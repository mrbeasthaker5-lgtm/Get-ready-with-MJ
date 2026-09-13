package com.example.data.model

enum class FaceShape(val displayName: String, val description: String) {
    OVAL("Oval", "Balanced length and width with gently curved jawline."),
    ROUND("Round", "Equal width and length with soft, fuller cheek curves."),
    SQUARE("Square", "Strong horizontal jawline with equal forehead and cheek width."),
    OBLONG("Oblong", "Longer face structure with uniform width throughout."),
    HEART("Heart", "Broader forehead tapering to a slender, pointed chin."),
    DIAMOND("Diamond", "Striking high cheekbones with narrower forehead and jaw."),
    TRIANGLE("Triangle", "Distinct jawline broader than the temples and forehead.")
}

enum class HairTexture(val displayName: String) {
    STRAIGHT("Straight"),
    WAVY("Wavy"),
    CURLY("Curly"),
    COILY("Coily"),
    NOT_SURE("Not sure")
}

enum class ImageValidationStatus(val message: String) {
    VALID("Image verified successfully."),
    NO_FACE("No face detected. Please upload a clear front-facing photo."),
    MULTIPLE_FACES("Please upload a photo containing only one face."),
    BLURRY("This photo is not clear enough for a reliable recommendation."),
    POOR_LIGHTING("Lighting is too dim or uneven. Please use bright, direct lighting."),
    OBSTRUCTED("Face is partially obstructed. Please remove sunglasses, masks, or hats.")
}

data class FacialGeometryMeasurements(
    val faceWidth: Float = 0f,
    val faceHeight: Float = 0f,
    val foreheadWidth: Float = 0f,
    val cheekboneWidth: Float = 0f,
    val jawWidth: Float = 0f,
    val chinRatio: Float = 0f,
    val faceLengthWidthRatio: Float = 1.35f,
    val jawCheekRatio: Float = 0.82f,
    val foreheadJawRatio: Float = 1.1f,
    val symmetryEstimate: Float = 0.94f
)

data class FaceShapeProbabilities(
    val oval: Float = 0.0f,
    val round: Float = 0.0f,
    val square: Float = 0.0f,
    val oblong: Float = 0.0f,
    val heart: Float = 0.0f,
    val diamond: Float = 0.0f,
    val triangle: Float = 0.0f
) {
    fun primaryShape(): FaceShape {
        val map = mapOf(
            FaceShape.OVAL to oval,
            FaceShape.ROUND to round,
            FaceShape.SQUARE to square,
            FaceShape.OBLONG to oblong,
            FaceShape.HEART to heart,
            FaceShape.DIAMOND to diamond,
            FaceShape.TRIANGLE to triangle
        )
        return map.maxByOrNull { it.value }?.key ?: FaceShape.OVAL
    }
}

data class HairVisualInfo(
    val approximateLength: String = "Medium",
    val visibleVolume: String = "Moderate",
    val visibleTexture: String = "Natural",
    val hairlineVisibility: String = "Visible"
)

data class FaceAnalysisResult(
    val validationStatus: ImageValidationStatus = ImageValidationStatus.VALID,
    val primaryFaceShape: FaceShape = FaceShape.OVAL,
    val shapeProbabilities: FaceShapeProbabilities = FaceShapeProbabilities(),
    val measurements: FacialGeometryMeasurements = FacialGeometryMeasurements(),
    val hairInfo: HairVisualInfo = HairVisualInfo(),
    val userTexture: HairTexture = HairTexture.NOT_SURE,
    val confidenceDescription: String = "Your face shape appears mostly Oval."
)
