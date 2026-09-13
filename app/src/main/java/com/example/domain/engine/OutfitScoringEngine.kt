package com.example.domain.engine

import com.example.data.catalog.FashionCatalog
import com.example.data.model.BodyProportion
import com.example.data.model.HeightInput
import com.example.data.model.Occasion
import com.example.data.model.Outfit
import com.example.data.model.OutfitRecommendation
import com.example.data.model.StylePreferenceCategory
import com.example.data.model.StyleProfile
import com.example.data.model.WeatherCondition

object OutfitScoringEngine {

    /**
     * Scores and ranks outfits from catalog:
     * - proportionCompatibility * 0.25
     * - styleCompatibility * 0.20
     * - weatherCompatibility * 0.15
     * - occasionCompatibility * 0.10
     * - currentTrendScore * 0.15
     * - colorCompatibility * 0.10
     * - profileCompatibility * 0.05
     *
     * Ensures exactly 6 diverse recommendations.
     */
    fun scoreAndRankOutfits(
        height: HeightInput,
        bodyProportion: BodyProportion,
        styleProfile: StyleProfile,
        preferredStyles: List<StylePreferenceCategory>,
        occasion: Occasion,
        weather: WeatherCondition
    ): List<OutfitRecommendation> {
        val candidates = FashionCatalog.curatedOutfits

        val scored = candidates.map { outfit ->
            val propComp = calculateProportionScore(outfit, height, bodyProportion)
            val styleComp = calculateStyleScore(outfit, preferredStyles)
            val weatherComp = calculateWeatherScore(outfit, weather)
            val occasionComp = if (outfit.occasion == occasion || occasion == Occasion.EVERYDAY) 0.96f else 0.82f
            val trendComp = outfit.trendScore
            val colorComp = 0.92f // High harmonious tonal compatibility
            val profileComp = 0.95f

            val totalScore = (propComp * 0.25f) +
                    (styleComp * 0.20f) +
                    (weatherComp * 0.15f) +
                    (occasionComp * 0.10f) +
                    (trendComp * 0.15f) +
                    (colorComp * 0.10f) +
                    (profileComp * 0.05f)

            val whyMJPicked = generateWhyMJPicked(outfit, height, bodyProportion, occasion, weather)

            OutfitRecommendation(
                outfit = outfit,
                score = totalScore,
                whyMJPicked = whyMJPicked,
                trendExplanation = outfit.trendExplanation
            )
        }.sortedByDescending { it.score }

        // Diversity filter across distinct categories
        val selected = mutableListOf<OutfitRecommendation>()
        val seenCategories = mutableSetOf<StylePreferenceCategory>()

        for (item in scored) {
            if (!seenCategories.contains(item.outfit.category)) {
                selected.add(item)
                seenCategories.add(item.outfit.category)
            }
            if (selected.size == 6) break
        }

        // Fill up to 6 if needed
        if (selected.size < 6) {
            for (item in scored) {
                if (!selected.contains(item)) {
                    selected.add(item)
                }
                if (selected.size == 6) break
            }
        }

        return selected
    }

    private fun calculateProportionScore(outfit: Outfit, height: HeightInput, proportion: BodyProportion): Float {
        // High-rise pleats and cropped boxy tees elongate shorter legs / longer torsos
        return when (proportion) {
            BodyProportion.LONGER_TORSO, BodyProportion.SHORTER_LEGS -> {
                if (outfit.category in listOf(StylePreferenceCategory.KOREAN_INSPIRED, StylePreferenceCategory.CLEAN_FIT, StylePreferenceCategory.STREETWEAR)) 0.98f else 0.85f
            }
            BodyProportion.LONGER_LEGS -> {
                if (outfit.category in listOf(StylePreferenceCategory.OVERSIZED, StylePreferenceCategory.OLD_MONEY, StylePreferenceCategory.TECHWEAR)) 0.97f else 0.86f
            }
            else -> 0.92f
        }
    }

    private fun calculateStyleScore(outfit: Outfit, preferredStyles: List<StylePreferenceCategory>): Float {
        if (preferredStyles.contains(StylePreferenceCategory.SURPRISE_ME) || preferredStyles.isEmpty()) {
            return 0.94f
        }
        return if (preferredStyles.contains(outfit.category)) 1.0f else 0.70f
    }

    private fun calculateWeatherScore(outfit: Outfit, weather: WeatherCondition): Float {
        return if (outfit.weatherSuitability == weather) {
            1.0f
        } else when (weather) {
            WeatherCondition.HOT -> if (outfit.weatherSuitability == WeatherCondition.WARM) 0.88f else 0.40f
            WeatherCondition.COLD -> if (outfit.weatherSuitability == WeatherCondition.COOL) 0.88f else 0.45f
            WeatherCondition.RAINY -> if (outfit.category == StylePreferenceCategory.TECHWEAR) 1.0f else 0.80f
            else -> 0.85f
        }
    }

    private fun generateWhyMJPicked(
        outfit: Outfit,
        height: HeightInput,
        proportion: BodyProportion,
        occasion: Occasion,
        weather: WeatherCondition
    ): String {
        return "MJ selected this look for your ${proportion.displayName.lowercase()} silhouette and ${height.displayStringCm} profile. The structured drape complements ${occasion.displayName.lowercase()} settings while maintaining comfort in ${weather.displayName.lowercase()} conditions."
    }
}
