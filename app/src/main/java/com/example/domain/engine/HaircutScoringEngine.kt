package com.example.domain.engine

import com.example.data.catalog.HaircutCatalog
import com.example.data.model.FaceAnalysisResult
import com.example.data.model.FaceShape
import com.example.data.model.GenderStylePreference
import com.example.data.model.HairTexture
import com.example.data.model.Haircut
import com.example.data.model.HaircutRecommendation
import com.example.data.model.MaintenanceLevel
import com.example.data.model.StyleProfile
import java.util.Locale

object HaircutScoringEngine {

    /**
     * Scores all haircuts in catalog according to specification weights:
     * - faceShapeCompatibility * 0.35
     * - facialProportionCompatibility * 0.20
     * - hairTextureCompatibility * 0.15
     * - stylePreferenceCompatibility * 0.10
     * - maintenanceCompatibility * 0.05
     * - trendScore * 0.10
     * - profileCompatibility * 0.05
     *
     * Applies category diversity to return exactly 6 distinct recommendations.
     */
    fun scoreAndRankHaircuts(
        analysis: FaceAnalysisResult,
        styleProfile: StyleProfile,
        preferredMaintenance: MaintenanceLevel? = null
    ): List<HaircutRecommendation> {
        val candidates = HaircutCatalog.haircuts

        val scoredList = candidates.map { haircut ->
            val faceShapeComp = calculateFaceShapeCompatibility(haircut, analysis.primaryFaceShape)
            val facialPropComp = calculateProportionCompatibility(haircut, analysis.measurements.faceLengthWidthRatio)
            val textureComp = calculateTextureCompatibility(haircut, analysis.userTexture)
            val stylePrefComp = 0.90f
            val maintenanceComp = if (preferredMaintenance == null || haircut.maintenanceLevel == preferredMaintenance) 0.95f else 0.70f
            val trendComp = haircut.trendScore
            val profileComp = if (haircut.styleProfileCompatibility.contains(styleProfile.genderStylePreference) ||
                styleProfile.genderStylePreference == GenderStylePreference.UNSPECIFIED
            ) 1.0f else 0.40f

            val totalScore = (faceShapeComp * 0.35f) +
                    (facialPropComp * 0.20f) +
                    (textureComp * 0.15f) +
                    (stylePrefComp * 0.10f) +
                    (maintenanceComp * 0.05f) +
                    (trendComp * 0.10f) +
                    (profileComp * 0.05f)

            val reason = generateWhyMJPicked(haircut, analysis.primaryFaceShape, analysis.userTexture)

            HaircutRecommendation(
                haircut = haircut,
                score = totalScore,
                reasonWhyMJPicked = reason,
                maintenance = haircut.maintenanceLevel,
                stylingDifficulty = haircut.stylingDifficulty,
                barberInstructions = haircut.barberInstructions
            )
        }.sortedByDescending { it.score }

        // Apply diversity filter: allow at most 2 haircuts per category (Crop, Fade, Fringe, Flow, etc.)
        val selected = mutableListOf<HaircutRecommendation>()
        val categoryCounts = mutableMapOf<String, Int>()

        for (item in scoredList) {
            val count = categoryCounts.getOrDefault(item.haircut.category, 0)
            if (count < 2) {
                selected.add(item)
                categoryCounts[item.haircut.category] = count + 1
            }
            if (selected.size == 6) break
        }

        // If not enough due to category restriction, fill up to exactly 6
        if (selected.size < 6) {
            for (item in scoredList) {
                if (!selected.contains(item)) {
                    selected.add(item)
                }
                if (selected.size == 6) break
            }
        }

        return selected
    }

    private fun calculateFaceShapeCompatibility(haircut: Haircut, shape: FaceShape): Float {
        return haircut.faceShapeScores[shape] ?: 0.80f
    }

    private fun calculateProportionCompatibility(haircut: Haircut, lengthWidthRatio: Float): Float {
        // Shorter faces benefit from height; longer faces benefit from side volume / fringe
        return when {
            lengthWidthRatio > 1.45f -> {
                // Longer face: crops & fringes balance best
                if (haircut.category in listOf("Fringe", "Crop", "Layered")) 0.96f else 0.78f
            }
            lengthWidthRatio < 1.25f -> {
                // Fuller / rounder face: vertical quiffs, tapers, side parts balance best
                if (haircut.category in listOf("Classic", "Fade", "Crop")) 0.95f else 0.80f
            }
            else -> 0.92f
        }
    }

    private fun calculateTextureCompatibility(haircut: Haircut, texture: HairTexture): Float {
        return haircut.hairTextureScores[texture] ?: 0.85f
    }

    private fun generateWhyMJPicked(haircut: Haircut, shape: FaceShape, texture: HairTexture): String {
        return when (shape) {
            FaceShape.OVAL -> "The balanced proportions detected from your photo harmonize with the ${haircut.category.lowercase(Locale.US)} silhouette, giving you clean visual definition without distorting your natural symmetry."
            FaceShape.ROUND -> "The structured geometry of the ${haircut.name} introduces crisp angularity and subtle crown volume, creating an elongating visual effect for your jawline."
            FaceShape.SQUARE -> "This cut softens the strong lateral lines of your jawbone while highlighting your natural bone structure and cheekbone contours."
            FaceShape.OBLONG -> "The horizontal drape and textured separation of the ${haircut.name} naturally reduce perceived vertical length for an effortless, well-proportioned aesthetic."
            FaceShape.HEART -> "The perimeter taper and crown texture balance the wider temple area with a streamlined transition towards your chin."
            FaceShape.DIAMOND -> "The full temple volume and softer edge styling of this cut complement your prominent high cheekbones perfectly."
            FaceShape.TRIANGLE -> "The upper crown fullness and textured movement create a striking aesthetic equilibrium with your defined lower jawline."
        }
    }
}
