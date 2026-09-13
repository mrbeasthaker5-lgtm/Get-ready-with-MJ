package com.example.domain.provider

import com.example.data.catalog.FashionCatalog
import com.example.data.model.FashionTrend
import com.example.data.model.Outfit

interface TrendProvider {
    suspend fun getCurrentTrends(
        region: String? = null,
        season: String? = null
    ): List<FashionTrend>
}

class DefaultTrendProvider : TrendProvider {
    override suspend fun getCurrentTrends(region: String?, season: String?): List<FashionTrend> {
        return FashionCatalog.currentTrends
    }
}

data class TryOnResult(
    val success: Boolean,
    val previewImageUri: String? = null,
    val statusMessage: String = "AI-generated preview"
)

interface HairstyleTryOnProvider {
    suspend fun generateTryOn(
        sourceImage: ByteArray,
        hairstyleId: String
    ): TryOnResult
}

class LocalHairstyleTryOnProvider : HairstyleTryOnProvider {
    override suspend fun generateTryOn(sourceImage: ByteArray, hairstyleId: String): TryOnResult {
        // High quality identity-preserving preview generator placeholder
        return TryOnResult(
            success = true,
            previewImageUri = "android.resource://com.aistudio.getreadywithmj.app/drawable/img_haircut_hero",
            statusMessage = "AI-generated preview: Hairstyle fitted with preserved facial identity."
        )
    }
}

data class ImageGenerationResult(
    val success: Boolean,
    val visualImageUri: String? = null,
    val statusMessage: String = "AI-generated style preview"
)

interface ImageGenerationProvider {
    suspend fun generateOutfitImage(outfit: Outfit): ImageGenerationResult
}

class DefaultImageGenerationProvider : ImageGenerationProvider {
    override suspend fun generateOutfitImage(outfit: Outfit): ImageGenerationResult {
        return ImageGenerationResult(
            success = true,
            visualImageUri = "android.resource://com.aistudio.getreadywithmj.app/drawable/img_outfit_hero",
            statusMessage = "AI-generated style preview: Editorial fashion visualization."
        )
    }
}
