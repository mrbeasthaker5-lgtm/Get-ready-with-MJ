package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.BodyProportion
import com.example.data.model.FaceAnalysisResult
import com.example.data.model.FaceShape
import com.example.data.model.GenderStylePreference
import com.example.data.model.HairTexture
import com.example.data.model.HeightInput
import com.example.data.model.Occasion
import com.example.data.model.StylePreferenceCategory
import com.example.data.model.StyleProfile
import com.example.data.model.WeatherCondition
import com.example.domain.engine.HaircutScoringEngine
import com.example.domain.engine.OutfitScoringEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Get Ready With MJ", appName)
  }

  @Test
  fun `haircut scoring engine produces top 6 recommendations`() {
    val analysis = FaceAnalysisResult(
      primaryFaceShape = FaceShape.OVAL,
      userTexture = HairTexture.WAVY
    )
    val styleProfile = StyleProfile(genderStylePreference = GenderStylePreference.UNSPECIFIED)
    val recs = HaircutScoringEngine.scoreAndRankHaircuts(analysis, styleProfile)

    assertEquals(6, recs.size)
    assertTrue(recs.first().score > 0.70f)
    assertNotNull(recs.first().reasonWhyMJPicked)
  }

  @Test
  fun `outfit scoring engine produces top 6 curated looks`() {
    val styleProfile = StyleProfile(genderStylePreference = GenderStylePreference.BOY)
    val recs = OutfitScoringEngine.scoreAndRankOutfits(
      height = HeightInput.fromFeetInches(5, 10),
      bodyProportion = BodyProportion.BALANCED,
      styleProfile = styleProfile,
      preferredStyles = listOf(StylePreferenceCategory.STREETWEAR),
      occasion = Occasion.EVERYDAY,
      weather = WeatherCondition.WARM
    )

    assertEquals(6, recs.size)
    assertTrue(recs.first().score > 0.60f)
    assertTrue(recs.first().outfit.garments.isNotEmpty())
  }

  @Test
  fun `camera navigation and photo capture updates active screen`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val vm = com.example.ui.MainViewModel(context as android.app.Application)

    vm.navigateTo(com.example.ui.ActiveScreen.HAIRCUT_CAMERA)
    assertEquals(com.example.ui.ActiveScreen.HAIRCUT_CAMERA, vm.uiState.value.activeScreen)

    val sampleBitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888)
    vm.onPhotoCaptured(sampleBitmap)

    assertEquals(com.example.ui.ActiveScreen.HAIRCUT_TEXTURE_PICKER, vm.uiState.value.activeScreen)
    assertNotNull(vm.uiState.value.capturedPhoto)
  }
}
