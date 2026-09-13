package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.camera.CameraCaptureScreen
import com.example.ui.screens.discover.DiscoverScreen
import com.example.ui.screens.haircut.HaircutInstructionsScreen
import com.example.ui.screens.haircut.HaircutLoadingScreen
import com.example.ui.screens.haircut.HaircutResultsScreen
import com.example.ui.screens.haircut.HaircutTextureScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.outfit.OutfitFlowScreen
import com.example.ui.screens.outfit.OutfitLoadingScreen
import com.example.ui.screens.outfit.OutfitResultsScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.saved.SavedScreen
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.ChampagneGoldLight
import com.example.ui.theme.GenZLilac
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface

@Composable
fun MainApp(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val savedLooks by viewModel.savedLooks.collectAsState()

    // Handle system back navigation
    BackHandler(enabled = uiState.activeScreen != ActiveScreen.MAIN_TABS) {
        when (uiState.activeScreen) {
            ActiveScreen.HAIRCUT_CAMERA -> viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS)
            ActiveScreen.HAIRCUT_TEXTURE_PICKER -> viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS)
            ActiveScreen.HAIRCUT_RESULTS -> viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS)
            ActiveScreen.OUTFIT_RESULTS -> viewModel.navigateTo(ActiveScreen.OUTFIT_FLOW)
            ActiveScreen.HAIRCUT_LOADING -> viewModel.navigateTo(ActiveScreen.HAIRCUT_TEXTURE_PICKER)
            ActiveScreen.OUTFIT_LOADING -> viewModel.navigateTo(ActiveScreen.OUTFIT_FLOW)
            else -> viewModel.navigateTo(ActiveScreen.MAIN_TABS)
        }
    }

    AnimatedContent(
        targetState = uiState.activeScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            ActiveScreen.ONBOARDING -> {
                OnboardingScreen(
                    onComplete = { viewModel.completeOnboarding() }
                )
            }

            ActiveScreen.MAIN_TABS -> {
                Scaffold(
                    modifier = modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                                .border(1.dp, ObsidianBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            NavigationBarItem(
                                selected = uiState.currentTab == AppNavTab.HOME,
                                onClick = { viewModel.setTab(AppNavTab.HOME) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.currentTab == AppNavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                        contentDescription = "Home"
                                    )
                                },
                                label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ChampagneGold,
                                    selectedTextColor = ChampagneGoldLight,
                                    indicatorColor = ChampagneGold.copy(alpha = 0.2f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("bottom_nav_home")
                            )

                            NavigationBarItem(
                                selected = uiState.currentTab == AppNavTab.DISCOVER,
                                onClick = { viewModel.setTab(AppNavTab.DISCOVER) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.currentTab == AppNavTab.DISCOVER) Icons.Filled.Explore else Icons.Outlined.Explore,
                                        contentDescription = "Discover"
                                    )
                                },
                                label = { Text("Discover", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ChampagneGold,
                                    selectedTextColor = ChampagneGoldLight,
                                    indicatorColor = ChampagneGold.copy(alpha = 0.2f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("bottom_nav_discover")
                            )

                            NavigationBarItem(
                                selected = uiState.currentTab == AppNavTab.SAVED,
                                onClick = { viewModel.setTab(AppNavTab.SAVED) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.currentTab == AppNavTab.SAVED) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = "Saved"
                                    )
                                },
                                label = { Text("Saved", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ChampagneGold,
                                    selectedTextColor = ChampagneGoldLight,
                                    indicatorColor = ChampagneGold.copy(alpha = 0.2f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("bottom_nav_saved")
                            )

                            NavigationBarItem(
                                selected = uiState.currentTab == AppNavTab.PROFILE,
                                onClick = { viewModel.setTab(AppNavTab.PROFILE) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.currentTab == AppNavTab.PROFILE) Icons.Filled.Person else Icons.Filled.PersonOutline,
                                        contentDescription = "Profile"
                                    )
                                },
                                label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ChampagneGold,
                                    selectedTextColor = ChampagneGoldLight,
                                    indicatorColor = ChampagneGold.copy(alpha = 0.2f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("bottom_nav_profile")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (uiState.currentTab) {
                            AppNavTab.HOME -> {
                                HomeScreen(
                                    onFindHaircutClick = { viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS) },
                                    onFindStyleClick = { viewModel.navigateTo(ActiveScreen.OUTFIT_FLOW) },
                                    onDiscoverClick = { viewModel.setTab(AppNavTab.DISCOVER) },
                                    savedLooks = savedLooks
                                )
                            }
                            AppNavTab.DISCOVER -> {
                                DiscoverScreen(
                                    onHaircutClick = { viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS) },
                                    onOutfitClick = { viewModel.navigateTo(ActiveScreen.OUTFIT_FLOW) }
                                )
                            }
                            AppNavTab.SAVED -> {
                                SavedScreen(
                                    savedLooks = savedLooks,
                                    currentFilter = uiState.savedFilter,
                                    onFilterChanged = { viewModel.setSavedFilter(it) },
                                    onDeleteLook = { viewModel.deleteSavedLookById(it) },
                                    onFindHaircutClick = { viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS) },
                                    onFindOutfitClick = { viewModel.navigateTo(ActiveScreen.OUTFIT_FLOW) }
                                )
                            }
                            AppNavTab.PROFILE -> {
                                ProfileScreen(
                                    currentPreference = uiState.styleProfile.genderStylePreference,
                                    onPreferenceSelected = { viewModel.setGlobalStylePreference(it) },
                                    isDarkMode = uiState.darkModeEnabled,
                                    onToggleDarkMode = { viewModel.toggleDarkMode(it) },
                                    isNotificationsEnabled = uiState.notificationsEnabled,
                                    onToggleNotifications = { viewModel.toggleNotifications(it) },
                                    onDeleteAllData = { viewModel.deleteAllUserData() },
                                    showDataDeletedMessage = uiState.showDataDeletedMessage
                                )
                            }
                        }
                    }
                }
            }

            ActiveScreen.HAIRCUT_INSTRUCTIONS -> {
                HaircutInstructionsScreen(
                    currentProfile = uiState.haircutProfile,
                    onProfileSelected = { viewModel.setHaircutProfile(it) },
                    onPhotoCaptured = { bitmap -> viewModel.onPhotoCaptured(bitmap) },
                    onOpenCameraClick = { viewModel.navigateTo(ActiveScreen.HAIRCUT_CAMERA) },
                    onBackClick = { viewModel.navigateTo(ActiveScreen.MAIN_TABS) },
                    errorMessage = uiState.haircutErrorMessage
                )
            }

            ActiveScreen.HAIRCUT_CAMERA -> {
                CameraCaptureScreen(
                    onPhotoCaptured = { bitmap -> viewModel.onPhotoCaptured(bitmap) },
                    onBackClick = { viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS) }
                )
            }

            ActiveScreen.HAIRCUT_TEXTURE_PICKER -> {
                HaircutTextureScreen(
                    capturedPhoto = uiState.capturedPhoto,
                    selectedTexture = uiState.haircutTexture,
                    onTextureSelected = { viewModel.setHaircutTexture(it) },
                    onContinueClick = { viewModel.startHaircutAnalysis() },
                    onBackClick = { viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS) }
                )
            }

            ActiveScreen.HAIRCUT_LOADING -> {
                HaircutLoadingScreen(currentStepText = uiState.hairLoadingStepText)
            }

            ActiveScreen.HAIRCUT_RESULTS -> {
                HaircutResultsScreen(
                    analysis = uiState.haircutAnalysis,
                    recommendations = uiState.recommendedHaircuts,
                    isSavedLook = { refId -> savedLooks.any { it.referenceId == refId } },
                    onSaveHaircut = { viewModel.saveHaircutLook(it) },
                    onRemoveSavedHaircut = { viewModel.removeSavedLook(it) },
                    onTryOnClick = { viewModel.triggerVirtualTryOn(it) },
                    isGeneratingTryOn = uiState.isGeneratingTryOn,
                    tryOnResult = uiState.tryOnResult,
                    onDismissTryOn = { viewModel.selectDetailHaircut(null) },
                    onFeedbackSubmitted = { rating, text -> viewModel.submitFeedback("HAIRCUT", rating, text) },
                    onRetakeClick = { viewModel.navigateTo(ActiveScreen.HAIRCUT_INSTRUCTIONS) },
                    onBackClick = { viewModel.navigateTo(ActiveScreen.MAIN_TABS) }
                )
            }

            ActiveScreen.OUTFIT_FLOW -> {
                OutfitFlowScreen(
                    currentProfile = uiState.outfitProfile,
                    onProfileSelected = { viewModel.setHaircutProfile(it) },
                    currentHeight = uiState.outfitHeight,
                    onHeightChanged = { viewModel.setOutfitHeight(it) },
                    currentProportion = uiState.outfitProportion,
                    onProportionSelected = { viewModel.setOutfitProportion(it) },
                    selectedStyles = uiState.outfitStyles,
                    onToggleStyle = { viewModel.toggleOutfitStyle(it) },
                    selectedOccasion = uiState.outfitOccasion,
                    onOccasionSelected = { viewModel.setOutfitOccasion(it) },
                    selectedWeather = uiState.outfitWeather,
                    onWeatherSelected = { viewModel.setOutfitWeather(it) },
                    onGenerateClick = { viewModel.startOutfitAnalysis() },
                    onBackClick = { viewModel.navigateTo(ActiveScreen.MAIN_TABS) }
                )
            }

            ActiveScreen.OUTFIT_LOADING -> {
                OutfitLoadingScreen(currentStepText = uiState.outfitLoadingStepText)
            }

            ActiveScreen.OUTFIT_RESULTS -> {
                OutfitResultsScreen(
                    recommendations = uiState.recommendedOutfits,
                    isSavedLook = { refId -> savedLooks.any { it.referenceId == refId } },
                    onSaveOutfit = { viewModel.saveOutfitLook(it) },
                    onRemoveSavedOutfit = { viewModel.removeSavedLook(it) },
                    onFeedbackSubmitted = { rating, text -> viewModel.submitFeedback("OUTFIT", rating, text) },
                    onRetakeClick = { viewModel.navigateTo(ActiveScreen.OUTFIT_FLOW) },
                    onBackClick = { viewModel.navigateTo(ActiveScreen.MAIN_TABS) }
                )
            }
        }
    }
}
