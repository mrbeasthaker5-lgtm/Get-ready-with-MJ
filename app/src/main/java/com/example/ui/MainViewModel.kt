package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.catalog.FashionCatalog
import com.example.data.catalog.HaircutCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.SavedLookEntity
import com.example.data.local.UserPreferencesEntity
import com.example.data.model.BodyProportion
import com.example.data.model.FaceAnalysisResult
import com.example.data.model.GenderStylePreference
import com.example.data.model.HairTexture
import com.example.data.model.HaircutRecommendation
import com.example.data.model.HeightInput
import com.example.data.model.ImageValidationStatus
import com.example.data.model.Occasion
import com.example.data.model.OutfitRecommendation
import com.example.data.model.StylePreferenceCategory
import com.example.data.model.StyleProfile
import com.example.data.model.WeatherCondition
import com.example.data.repository.StylingRepository
import com.example.domain.provider.TryOnResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppNavTab {
    HOME, DISCOVER, SAVED, PROFILE
}

enum class ActiveScreen {
    MAIN_TABS,
    HAIRCUT_INSTRUCTIONS,
    HAIRCUT_CAMERA,
    HAIRCUT_TEXTURE_PICKER,
    HAIRCUT_LOADING,
    HAIRCUT_RESULTS,
    OUTFIT_FLOW,
    OUTFIT_LOADING,
    OUTFIT_RESULTS,
    ONBOARDING
}

data class UiState(
    val currentTab: AppNavTab = AppNavTab.HOME,
    val activeScreen: ActiveScreen = ActiveScreen.MAIN_TABS,
    val isOnboardingComplete: Boolean = true,
    val styleProfile: StyleProfile = StyleProfile(GenderStylePreference.UNSPECIFIED),

    // Haircut Finder State
    val haircutProfile: GenderStylePreference = GenderStylePreference.UNSPECIFIED,
    val haircutTexture: HairTexture = HairTexture.NOT_SURE,
    val capturedPhoto: Bitmap? = null,
    val isAnalyzingHair: Boolean = false,
    val hairLoadingStepText: String = "Analyzing your photo...",
    val haircutAnalysis: FaceAnalysisResult? = null,
    val recommendedHaircuts: List<HaircutRecommendation> = emptyList(),
    val haircutErrorMessage: String? = null,
    val detailHaircut: HaircutRecommendation? = null,
    val tryOnResult: TryOnResult? = null,
    val isGeneratingTryOn: Boolean = false,

    // Outfit Finder State
    val outfitHeight: HeightInput = HeightInput(175f),
    val outfitProportion: BodyProportion = BodyProportion.BALANCED,
    val outfitProfile: GenderStylePreference = GenderStylePreference.UNSPECIFIED,
    val outfitStyles: Set<StylePreferenceCategory> = setOf(StylePreferenceCategory.STREETWEAR, StylePreferenceCategory.CLEAN_FIT),
    val outfitOccasion: Occasion = Occasion.EVERYDAY,
    val outfitWeather: WeatherCondition = WeatherCondition.WARM,
    val isAnalyzingOutfit: Boolean = false,
    val outfitLoadingStepText: String = "Building your style profile...",
    val recommendedOutfits: List<OutfitRecommendation> = emptyList(),
    val outfitErrorMessage: String? = null,
    val detailOutfit: OutfitRecommendation? = null,

    // Saved Screen
    val savedFilter: String = "ALL", // "ALL", "HAIRCUT", "OUTFIT"
    val detailSavedItem: SavedLookEntity? = null,

    // Feedback & System
    val feedbackSubmitted: Boolean = false,
    val darkModeEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val showDataDeletedMessage: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StylingRepository

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val savedLooks: StateFlow<List<SavedLookEntity>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = StylingRepository(db)
        savedLooks = repository.savedLooks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Observe stored user preferences
        viewModelScope.launch {
            repository.userPreferences.collect { entity ->
                if (entity != null) {
                    val pref = try {
                        GenderStylePreference.valueOf(entity.genderPreference)
                    } catch (_: Exception) {
                        GenderStylePreference.UNSPECIFIED
                    }
                    val prop = try {
                        BodyProportion.valueOf(entity.proportion)
                    } catch (_: Exception) {
                        BodyProportion.BALANCED
                    }
                    _uiState.update {
                        it.copy(
                            styleProfile = StyleProfile(pref),
                            outfitProfile = pref,
                            haircutProfile = pref,
                            outfitHeight = HeightInput(entity.heightCm),
                            outfitProportion = prop,
                            darkModeEnabled = entity.darkTheme,
                            notificationsEnabled = entity.notifications
                        )
                    }
                }
            }
        }
    }

    fun setTab(tab: AppNavTab) {
        _uiState.update { it.copy(currentTab = tab, activeScreen = ActiveScreen.MAIN_TABS) }
    }

    fun navigateTo(screen: ActiveScreen) {
        _uiState.update { it.copy(activeScreen = screen) }
    }

    fun completeOnboarding() {
        _uiState.update { it.copy(isOnboardingComplete = true, activeScreen = ActiveScreen.MAIN_TABS) }
    }

    fun setGlobalStylePreference(preference: GenderStylePreference) {
        _uiState.update {
            it.copy(
                styleProfile = StyleProfile(preference),
                haircutProfile = preference,
                outfitProfile = preference
            )
        }
        persistPreferences()
    }

    // --- Haircut Finder Actions ---

    fun setHaircutProfile(preference: GenderStylePreference) {
        _uiState.update { it.copy(haircutProfile = preference) }
    }

    fun setHaircutTexture(texture: HairTexture) {
        _uiState.update { it.copy(haircutTexture = texture) }
    }

    fun onPhotoCaptured(bitmap: Bitmap) {
        _uiState.update {
            it.copy(
                capturedPhoto = bitmap,
                activeScreen = ActiveScreen.HAIRCUT_TEXTURE_PICKER,
                haircutErrorMessage = null
            )
        }
    }

    fun startHaircutAnalysis() {
        val bitmap = _uiState.value.capturedPhoto
        if (bitmap == null) {
            _uiState.update { it.copy(haircutErrorMessage = "Please capture or select a photo first.") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAnalyzingHair = true,
                    activeScreen = ActiveScreen.HAIRCUT_LOADING,
                    hairLoadingStepText = "Analyzing your photo..."
                )
            }

            val steps = listOf(
                "Analyzing your photo...",
                "Mapping facial proportions...",
                "Checking hairstyle compatibility...",
                "Finding your best matches...",
                "Creating your Top 6..."
            )

            for (step in steps) {
                _uiState.update { it.copy(hairLoadingStepText = step) }
                delay(650)
            }

            try {
                val (analysis, recommendations) = repository.analyzeFaceAndRecommendHaircuts(
                    bitmap = bitmap,
                    texture = _uiState.value.haircutTexture,
                    styleProfile = StyleProfile(_uiState.value.haircutProfile)
                )

                if (analysis.validationStatus != ImageValidationStatus.VALID) {
                    _uiState.update {
                        it.copy(
                            isAnalyzingHair = false,
                            haircutErrorMessage = analysis.validationStatus.message,
                            activeScreen = ActiveScreen.HAIRCUT_INSTRUCTIONS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isAnalyzingHair = false,
                            haircutAnalysis = analysis,
                            recommendedHaircuts = recommendations,
                            activeScreen = ActiveScreen.HAIRCUT_RESULTS
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isAnalyzingHair = false,
                        haircutErrorMessage = "Failed to analyze photo: ${e.localizedMessage ?: "Unknown error"}",
                        activeScreen = ActiveScreen.HAIRCUT_INSTRUCTIONS
                    )
                }
            }
        }
    }

    fun selectDetailHaircut(haircut: HaircutRecommendation?) {
        _uiState.update { it.copy(detailHaircut = haircut, tryOnResult = null) }
    }

    fun triggerVirtualTryOn(haircut: HaircutRecommendation) {
        val bitmap = _uiState.value.capturedPhoto ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingTryOn = true) }
            val result = repository.generateVirtualTryOn(bitmap, haircut.haircut.id)
            _uiState.update { it.copy(isGeneratingTryOn = false, tryOnResult = result) }
        }
    }

    // --- Outfit Finder Actions ---

    fun setOutfitHeight(height: HeightInput) {
        _uiState.update { it.copy(outfitHeight = height) }
        persistPreferences()
    }

    fun setOutfitProportion(proportion: BodyProportion) {
        _uiState.update { it.copy(outfitProportion = proportion) }
        persistPreferences()
    }

    fun toggleOutfitStyle(style: StylePreferenceCategory) {
        _uiState.update { state ->
            val updated = state.outfitStyles.toMutableSet()
            if (style == StylePreferenceCategory.SURPRISE_ME) {
                updated.clear()
                updated.add(StylePreferenceCategory.SURPRISE_ME)
            } else {
                updated.remove(StylePreferenceCategory.SURPRISE_ME)
                if (updated.contains(style)) {
                    updated.remove(style)
                } else {
                    updated.add(style)
                }
            }
            state.copy(outfitStyles = updated)
        }
    }

    fun setOutfitOccasion(occasion: Occasion) {
        _uiState.update { it.copy(outfitOccasion = occasion) }
    }

    fun setOutfitWeather(weather: WeatherCondition) {
        _uiState.update { it.copy(outfitWeather = weather) }
    }

    fun startOutfitAnalysis() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAnalyzingOutfit = true,
                    activeScreen = ActiveScreen.OUTFIT_LOADING,
                    outfitLoadingStepText = "Building your style profile..."
                )
            }

            val steps = listOf(
                "Building your style profile...",
                "Checking current trends...",
                "Matching silhouettes...",
                "Creating outfit combinations...",
                "Ranking your Top 6..."
            )

            for (step in steps) {
                _uiState.update { it.copy(outfitLoadingStepText = step) }
                delay(650)
            }

            try {
                val recommendations = repository.recommendOutfits(
                    height = _uiState.value.outfitHeight,
                    bodyProportion = _uiState.value.outfitProportion,
                    styleProfile = StyleProfile(_uiState.value.outfitProfile),
                    preferredStyles = _uiState.value.outfitStyles.toList(),
                    occasion = _uiState.value.outfitOccasion,
                    weather = _uiState.value.outfitWeather
                )

                _uiState.update {
                    it.copy(
                        isAnalyzingOutfit = false,
                        recommendedOutfits = recommendations,
                        activeScreen = ActiveScreen.OUTFIT_RESULTS
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isAnalyzingOutfit = false,
                        outfitErrorMessage = "Failed to generate outfit recommendations: ${e.localizedMessage ?: "Unknown error"}",
                        activeScreen = ActiveScreen.OUTFIT_FLOW
                    )
                }
            }
        }
    }

    fun selectDetailOutfit(outfit: OutfitRecommendation?) {
        _uiState.update { it.copy(detailOutfit = outfit) }
    }

    // --- Saved Looks Actions ---

    fun saveHaircutLook(recommendation: HaircutRecommendation) {
        viewModelScope.launch {
            repository.saveLook(
                lookType = "HAIRCUT",
                referenceId = recommendation.haircut.id,
                title = recommendation.haircut.name,
                subtitle = recommendation.haircut.category,
                tags = "${recommendation.maintenance.label} • ${recommendation.stylingDifficulty.label}",
                details = recommendation.reasonWhyMJPicked,
                imageUrl = recommendation.haircut.imageUrl
            )
        }
    }

    fun saveOutfitLook(recommendation: OutfitRecommendation) {
        viewModelScope.launch {
            repository.saveLook(
                lookType = "OUTFIT",
                referenceId = recommendation.outfit.id,
                title = recommendation.outfit.name,
                subtitle = recommendation.outfit.category.displayName,
                tags = "${recommendation.outfit.occasion.displayName} • ${recommendation.outfit.weatherSuitability.displayName}",
                details = recommendation.whyMJPicked,
                imageUrl = recommendation.outfit.previewResName
            )
        }
    }

    fun removeSavedLook(refId: String) {
        viewModelScope.launch {
            repository.removeSavedLook(refId)
        }
    }

    fun deleteSavedLookById(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedLookById(id)
            if (_uiState.value.detailSavedItem?.id == id) {
                _uiState.update { it.copy(detailSavedItem = null) }
            }
        }
    }

    fun setSavedFilter(filter: String) {
        _uiState.update { it.copy(savedFilter = filter) }
    }

    fun selectDetailSavedItem(item: SavedLookEntity?) {
        _uiState.update { it.copy(detailSavedItem = item) }
    }

    // --- Feedback & Settings ---

    fun submitFeedback(type: String, rating: String, comment: String) {
        viewModelScope.launch {
            repository.saveFeedback(type, rating, comment)
            _uiState.update { it.copy(feedbackSubmitted = true) }
            delay(2500)
            _uiState.update { it.copy(feedbackSubmitted = false) }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(darkModeEnabled = enabled) }
        persistPreferences()
    }

    fun toggleNotifications(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
        persistPreferences()
    }

    fun deleteAllUserData() {
        viewModelScope.launch {
            repository.clearAllData()
            _uiState.update {
                it.copy(
                    detailSavedItem = null,
                    recommendedHaircuts = emptyList(),
                    recommendedOutfits = emptyList(),
                    capturedPhoto = null,
                    showDataDeletedMessage = true
                )
            }
            delay(3000)
            _uiState.update { it.copy(showDataDeletedMessage = false) }
        }
    }

    private fun persistPreferences() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.updateUserPreferences(
                UserPreferencesEntity(
                    id = 1,
                    genderPreference = state.styleProfile.genderStylePreference.name,
                    heightCm = state.outfitHeight.heightCm,
                    proportion = state.outfitProportion.name,
                    hairTexture = state.haircutTexture.name,
                    preferredStyles = state.outfitStyles.joinToString(",") { it.name },
                    darkTheme = state.darkModeEnabled,
                    notifications = state.notificationsEnabled
                )
            )
        }
    }
}
