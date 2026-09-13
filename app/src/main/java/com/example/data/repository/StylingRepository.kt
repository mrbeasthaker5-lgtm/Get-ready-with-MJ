package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.local.AppDatabase
import com.example.data.local.SavedLookEntity
import com.example.data.local.UserFeedbackEntity
import com.example.data.local.UserPreferencesEntity
import com.example.data.model.BodyProportion
import com.example.data.model.FaceAnalysisResult
import com.example.data.model.HairTexture
import com.example.data.model.HaircutRecommendation
import com.example.data.model.HeightInput
import com.example.data.model.Occasion
import com.example.data.model.OutfitRecommendation
import com.example.data.model.StylePreferenceCategory
import com.example.data.model.StyleProfile
import com.example.data.model.WeatherCondition
import com.example.domain.engine.FacialGeometryAnalyzer
import com.example.domain.engine.HaircutScoringEngine
import com.example.domain.engine.OutfitScoringEngine
import com.example.domain.provider.DefaultImageGenerationProvider
import com.example.domain.provider.DefaultTrendProvider
import com.example.domain.provider.HairstyleTryOnProvider
import com.example.domain.provider.ImageGenerationProvider
import com.example.domain.provider.LocalHairstyleTryOnProvider
import com.example.domain.provider.TrendProvider
import com.example.domain.provider.TryOnResult
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream

class StylingRepository(
    private val database: AppDatabase,
    private val trendProvider: TrendProvider = DefaultTrendProvider(),
    private val tryOnProvider: HairstyleTryOnProvider = LocalHairstyleTryOnProvider(),
    private val imageGenerationProvider: ImageGenerationProvider = DefaultImageGenerationProvider()
) {
    val savedLooks: Flow<List<SavedLookEntity>> = database.savedLookDao().getAllSavedLooks()
    val userPreferences: Flow<UserPreferencesEntity?> = database.userPreferencesDao().getUserPreferences()

    fun isLookSaved(refId: String): Flow<Boolean> = database.savedLookDao().isLookSaved(refId)

    suspend fun saveLook(
        lookType: String,
        referenceId: String,
        title: String,
        subtitle: String,
        tags: String,
        details: String,
        imageUrl: String
    ): Long {
        return database.savedLookDao().insertSavedLook(
            SavedLookEntity(
                lookType = lookType,
                referenceId = referenceId,
                title = title,
                subtitle = subtitle,
                tags = tags,
                details = details,
                imageUrl = imageUrl
            )
        )
    }

    suspend fun removeSavedLook(refId: String) {
        database.savedLookDao().deleteByReferenceId(refId)
    }

    suspend fun deleteSavedLookById(id: Long) {
        database.savedLookDao().deleteById(id)
    }

    suspend fun clearAllData() {
        database.savedLookDao().clearAll()
    }

    suspend fun saveFeedback(type: String, rating: String, text: String): Long {
        return database.userFeedbackDao().insertFeedback(
            UserFeedbackEntity(
                recommendationType = type,
                rating = rating,
                feedbackText = text
            )
        )
    }

    suspend fun updateUserPreferences(entity: UserPreferencesEntity) {
        database.userPreferencesDao().savePreferences(entity)
    }

    suspend fun analyzeFaceAndRecommendHaircuts(
        bitmap: Bitmap,
        texture: HairTexture,
        styleProfile: StyleProfile
    ): Pair<FaceAnalysisResult, List<HaircutRecommendation>> {
        val analysis = FacialGeometryAnalyzer.analyzeFace(bitmap, texture)
        val recommendations = HaircutScoringEngine.scoreAndRankHaircuts(analysis, styleProfile)
        return Pair(analysis, recommendations)
    }

    suspend fun recommendOutfits(
        height: HeightInput,
        bodyProportion: BodyProportion,
        styleProfile: StyleProfile,
        preferredStyles: List<StylePreferenceCategory>,
        occasion: Occasion,
        weather: WeatherCondition
    ): List<OutfitRecommendation> {
        return OutfitScoringEngine.scoreAndRankOutfits(
            height = height,
            bodyProportion = bodyProportion,
            styleProfile = styleProfile,
            preferredStyles = preferredStyles,
            occasion = occasion,
            weather = weather
        )
    }

    suspend fun generateVirtualTryOn(bitmap: Bitmap, haircutId: String): TryOnResult {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
        val byteArray = stream.toByteArray()
        return tryOnProvider.generateTryOn(byteArray, haircutId)
    }
}
