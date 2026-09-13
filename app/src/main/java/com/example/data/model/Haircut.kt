package com.example.data.model

enum class MaintenanceLevel(val label: String) {
    LOW("Low Maintenance (Cut every 4-6 wks)"),
    MEDIUM("Medium Maintenance (Cut every 2-3 wks)"),
    HIGH("High Maintenance (Cut every 1-2 wks)")
}

enum class StylingDifficulty(val label: String) {
    EASY("Easy (Wash & Go / 2 mins)"),
    MEDIUM("Medium (Blowdry / Matte Clay / 5 mins)"),
    ADVANCED("Advanced (Precision Blowout / Sea Salt + Texture Powder / 10 mins)")
}

data class Haircut(
    val id: String,
    val name: String,
    val category: String, // Fade, Crop, Fringe, Classic, Flow, Layered, Buzz
    val description: String,
    val minLength: String,
    val maxLength: String,
    val maintenanceLevel: MaintenanceLevel,
    val stylingDifficulty: StylingDifficulty,
    val faceShapeScores: Map<FaceShape, Float>,
    val hairTextureScores: Map<HairTexture, Float>,
    val trendScore: Float, // 0.0 - 1.0
    val styleProfileCompatibility: List<GenderStylePreference>,
    val barberInstructions: String,
    val imageUrl: String = "",
    val imageResName: String = "img_haircut_hero"
)

data class HaircutRecommendation(
    val haircut: Haircut,
    val score: Float,
    val reasonWhyMJPicked: String,
    val maintenance: MaintenanceLevel,
    val stylingDifficulty: StylingDifficulty,
    val barberInstructions: String,
    val isTryOnGenerated: Boolean = false,
    val tryOnImageUri: String? = null
)
