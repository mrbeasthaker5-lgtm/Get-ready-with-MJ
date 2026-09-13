package com.example.data.model

import java.util.Locale

data class HeightInput(
    val heightCm: Float = 175f
) {
    companion object {
        fun fromFeetInches(feet: Int, inches: Int): HeightInput {
            val totalInches = (feet * 12) + inches
            return HeightInput(totalInches * 2.54f)
        }

        fun fromCm(cm: Float): HeightInput {
            return HeightInput(cm)
        }
    }

    val feetAndInches: Pair<Int, Int>
        get() {
            val totalInches = (heightCm / 2.54f).toInt()
            val feet = totalInches / 12
            val inches = totalInches % 12
            return Pair(feet, inches)
        }

    val displayStringCm: String
        get() = String.format(Locale.US, "%.0f cm", heightCm)

    val displayStringFtIn: String
        get() {
            val (feet, inches) = feetAndInches
            return "$feet\'$inches\""
        }
}

enum class BodyProportion(val displayName: String, val tip: String) {
    BALANCED("Balanced", "Symmetric torso-to-leg ratio"),
    LONGER_LEGS("Longer Legs", "Higher natural waistline, suited for relaxed drape"),
    LONGER_TORSO("Longer Torso", "Lower natural waistline, great for cropped or tucked layers"),
    SHORTER_LEGS("Shorter Legs", "Accented by high-waisted bottoms and vertical lines"),
    NOT_SURE("Not Sure", "Versatile proportions with balanced silhouettes")
}

enum class StylePreferenceCategory(val displayName: String, val aestheticVibe: String) {
    STREETWEAR("Streetwear", "Graphic heavy, boxy silhouettes & statement sneakers"),
    MINIMAL("Minimal", "Clean lines, muted neutrals, uncluttered cuts"),
    Y2K("Y2K", "Cyber metallic accents, low slung fits, retro sporty"),
    KOREAN_INSPIRED("Korean-inspired", "Cropped boxy blazers, wide slacks, pristine drape"),
    OLD_MONEY("Old Money", "Loro-piana knitwear, linen trousers, understated luxury"),
    CASUAL("Casual", "Effortless everyday denim, quality basics, daily comfort"),
    SMART_CASUAL("Smart Casual", "Structured overshirts, tailored chinos, loafers"),
    TECHWEAR("Techwear", "Functional nylon, cargo modularity, weather-shield utility"),
    VINTAGE("Vintage", "Sun-faded washes, throwback typography, archival cuts"),
    SKATE("Skate", "Durable workwear canvas, relaxed carpenter pants, waffle soles"),
    ATHLEISURE("Athleisure", "Heavyweight fleece, ribbed sets, modern training cuts"),
    CLEAN_FIT("Clean Fit", "Tonal palettes, immaculate proportions, crisp footwear"),
    OVERSIZED("Oversized", "Relaxed dropped shoulders, generous hems, volume drape"),
    PREPPY("Preppy", "Varsity jackets, polo collars, pleated shorts, loafers"),
    EDGY("Edgy", "Distressed leather, hardware accents, dark monochrome"),
    SURPRISE_ME("Surprise Me", "MJ's curated cross-genre signature mix")
}

enum class Occasion(val displayName: String) {
    EVERYDAY("Everyday"),
    COLLEGE("College / Campus"),
    DATE("Date Night"),
    PARTY("Party"),
    FESTIVAL("Festival"),
    VACATION("Vacation / Resort"),
    WEDDING("Wedding / Formal"),
    CASUAL_HANGOUT("Casual Hangout"),
    NIGHT_OUT("Night Out"),
    INTERVIEW("Interview / Job"),
    GYM("Gym / Active")
}

enum class WeatherCondition(val displayName: String, val emoji: String) {
    HOT("Hot (>28°C)", "☀️"),
    WARM("Warm (20-27°C)", "🌤️"),
    COOL("Cool (13-19°C)", "⛅"),
    COLD("Cold (<12°C)", "❄️"),
    RAINY("Rainy / Wet", "🌧️")
}

data class FashionTrend(
    val id: String,
    val name: String,
    val category: String,
    val region: String = "Global",
    val season: String = "FW26",
    val trendScore: Float = 0.90f,
    val source: String = "MJ Global Trend Radar",
    val updatedAt: String = "Today"
)

data class Garment(
    val id: String,
    val category: String, // T-Shirt, Shirt, Hoodie, Jacket, Overshirt, Jeans, Wide-Leg Pants, Cargo Pants, Shorts, Sneakers, Boots, Accessories
    val name: String,
    val colors: List<String>,
    val silhouette: String, // Boxy, Oversized, Slim, Regular, Relaxed, Flared
    val styleTags: List<StylePreferenceCategory>,
    val weatherTags: List<WeatherCondition>,
    val occasionTags: List<Occasion>,
    val priceLevel: String = "$$",
    val trendScore: Float = 0.85f
)

data class Outfit(
    val id: String,
    val name: String,
    val category: StylePreferenceCategory,
    val garments: List<Garment>,
    val colors: List<String>,
    val shoes: String,
    val accessories: String,
    val occasion: Occasion,
    val weatherSuitability: WeatherCondition,
    val trendExplanation: String,
    val stylingExplanation: String,
    val trendScore: Float,
    val previewResName: String = "img_outfit_hero"
)

data class OutfitRecommendation(
    val outfit: Outfit,
    val score: Float,
    val whyMJPicked: String,
    val trendExplanation: String,
    val isTryOnGenerated: Boolean = false,
    val generatedVisualUri: String? = null
)
