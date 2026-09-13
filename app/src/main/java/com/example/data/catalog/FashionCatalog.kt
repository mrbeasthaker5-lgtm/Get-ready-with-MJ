package com.example.data.catalog

import com.example.data.model.FashionTrend
import com.example.data.model.Garment
import com.example.data.model.Occasion
import com.example.data.model.Outfit
import com.example.data.model.StylePreferenceCategory
import com.example.data.model.WeatherCondition

object FashionCatalog {

    val currentTrends: List<FashionTrend> = listOf(
        FashionTrend(
            id = "trend_boxy_proportions",
            name = "Boxy Cropped Tops + Wide-Leg Drapes",
            category = "Silhouette",
            region = "Global / East Asia / Western Metro",
            season = "FW26",
            trendScore = 0.98f,
            source = "MJ Fashion Radar & Streetwear Pulse"
        ),
        FashionTrend(
            id = "trend_vintage_wash",
            name = "Sun-Faded & Mineral Washed Heavyweight Cottons",
            category = "Material & Texture",
            region = "Global",
            season = "FW26",
            trendScore = 0.94f,
            source = "MJ Trend Analytics"
        ),
        FashionTrend(
            id = "trend_retro_sneakers",
            name = "Low-Profile Retro Terrace & 2000s Runners",
            category = "Footwear",
            region = "Global",
            season = "FW26",
            trendScore = 0.96f,
            source = "Footwear Culture Monitor"
        ),
        FashionTrend(
            id = "trend_quiet_workwear",
            name = "Refined Workwear & Carpenter Utility",
            category = "Bottoms",
            region = "North America / Europe",
            season = "FW26",
            trendScore = 0.93f,
            source = "Global Street Style"
        ),
        FashionTrend(
            id = "trend_tonal_layering",
            name = "Monochrome & Earth-Tonal Micro-Layering",
            category = "Colorway",
            region = "Global",
            season = "FW26",
            trendScore = 0.91f,
            source = "Runway to Sidewalk Index"
        )
    )

    val garments: List<Garment> = listOf(
        Garment(
            id = "gar_tee_boxy_charcoal",
            category = "T-Shirt",
            name = "Heavyweight 280GSM Boxy Drop-Shoulder Tee",
            colors = listOf("Washed Charcoal", "Off-White", "Sage"),
            silhouette = "Boxy Oversized",
            styleTags = listOf(StylePreferenceCategory.STREETWEAR, StylePreferenceCategory.CLEAN_FIT, StylePreferenceCategory.SKATE),
            weatherTags = listOf(WeatherCondition.HOT, WeatherCondition.WARM, WeatherCondition.COOL),
            occasionTags = listOf(Occasion.EVERYDAY, Occasion.COLLEGE, Occasion.CASUAL_HANGOUT),
            priceLevel = "$$",
            trendScore = 0.97f
        ),
        Garment(
            id = "gar_jeans_wide_vintage",
            category = "Wide-Leg Pants",
            name = "Vintage Tint Baggy Denim with Knee Darts",
            colors = listOf("Washed Indigo", "Faded Grey", "Raw Ecru"),
            silhouette = "Wide-Leg Puddle",
            styleTags = listOf(StylePreferenceCategory.STREETWEAR, StylePreferenceCategory.Y2K, StylePreferenceCategory.SKATE),
            weatherTags = listOf(WeatherCondition.WARM, WeatherCondition.COOL, WeatherCondition.COLD),
            occasionTags = listOf(Occasion.EVERYDAY, Occasion.COLLEGE, Occasion.CASUAL_HANGOUT, Occasion.PARTY),
            priceLevel = "$$$",
            trendScore = 0.96f
        ),
        Garment(
            id = "gar_jacket_workwear_canvas",
            category = "Jacket",
            name = "Corduroy-Collar Washed Canvas Detroit Jacket",
            colors = listOf("Oatmeal Khaki", "Olive Drab", "Faded Black"),
            silhouette = "Cropped Boxy",
            styleTags = listOf(StylePreferenceCategory.STREETWEAR, StylePreferenceCategory.VINTAGE, StylePreferenceCategory.CLEAN_FIT),
            weatherTags = listOf(WeatherCondition.COOL, WeatherCondition.COLD, WeatherCondition.RAINY),
            occasionTags = listOf(Occasion.EVERYDAY, Occasion.DATE, Occasion.CASUAL_HANGOUT, Occasion.NIGHT_OUT),
            priceLevel = "$$$",
            trendScore = 0.95f
        ),
        Garment(
            id = "gar_knit_halfzip_minimal",
            category = "Sweater",
            name = "Merino Waffle Knit Half-Zip Pullover",
            colors = listOf("Cream Melange", "Charcoal Heather", "Navy"),
            silhouette = "Relaxed Tailored",
            styleTags = listOf(StylePreferenceCategory.OLD_MONEY, StylePreferenceCategory.MINIMAL, StylePreferenceCategory.SMART_CASUAL),
            weatherTags = listOf(WeatherCondition.COOL, WeatherCondition.COLD),
            occasionTags = listOf(Occasion.DATE, Occasion.COLLEGE, Occasion.INTERVIEW, Occasion.EVERYDAY),
            priceLevel = "$$$",
            trendScore = 0.92f
        ),
        Garment(
            id = "gar_pants_pleated_slacks",
            category = "Pants",
            name = "Double-Pleated Relaxed Fluid Wool-Blend Trousers",
            colors = listOf("Espresso Brown", "Slate Charcoal", "Sand"),
            silhouette = "Fluid Drape Wide",
            styleTags = listOf(StylePreferenceCategory.KOREAN_INSPIRED, StylePreferenceCategory.OLD_MONEY, StylePreferenceCategory.CLEAN_FIT),
            weatherTags = listOf(WeatherCondition.WARM, WeatherCondition.COOL, WeatherCondition.COLD),
            occasionTags = listOf(Occasion.DATE, Occasion.INTERVIEW, Occasion.PARTY, Occasion.WEDDING),
            priceLevel = "$$$",
            trendScore = 0.94f
        ),
        Garment(
            id = "gar_hoodie_heavy_mineral",
            category = "Hoodie",
            name = "Mineral Washed 460GSM Double-Layer Hood Pullover",
            colors = listOf("Washed Mocha", "Faded Slate", "Bone"),
            silhouette = "Cropped Wide-Cut",
            styleTags = listOf(StylePreferenceCategory.STREETWEAR, StylePreferenceCategory.OVERSIZED, StylePreferenceCategory.ATHLEISURE),
            weatherTags = listOf(WeatherCondition.COOL, WeatherCondition.COLD),
            occasionTags = listOf(Occasion.EVERYDAY, Occasion.COLLEGE, Occasion.CASUAL_HANGOUT, Occasion.GYM),
            priceLevel = "$$",
            trendScore = 0.95f
        )
    )

    val curatedOutfits: List<Outfit> = listOf(
        Outfit(
            id = "outfit_relaxed_streetwear",
            name = "Relaxed Minimal Streetwear",
            category = StylePreferenceCategory.STREETWEAR,
            garments = listOf(garments[0], garments[1]),
            colors = listOf("#27272A", "#64748B", "#F1F5F9"),
            shoes = "Low-top retro chunky gum-sole runners (Metallic silver / Grey)",
            accessories = "Chunky brushed silver curb chain, square-frame acetate sunglasses, canvas mini tote",
            occasion = Occasion.EVERYDAY,
            weatherSuitability = WeatherCondition.WARM,
            trendExplanation = "Anchored by 2026's dominant silhouette: boxy cropped torso balanced against relaxed wide-drape leg pools.",
            stylingExplanation = "The dropped shoulders broaden the chest visual line while puddle-leg hems create effortless movement.",
            trendScore = 0.97f
        ),
        Outfit(
            id = "outfit_clean_workwear_layer",
            name = "Urban Utility Canvas Fit",
            category = StylePreferenceCategory.CLEAN_FIT,
            garments = listOf(garments[0], garments[2], garments[1]),
            colors = listOf("#475569", "#A8A29E", "#1E293B"),
            shoes = "Matte leather moc-toe lug sole work boots",
            accessories = "Brushed stainless steel automatic watch, corduroy flat cap",
            occasion = Occasion.CASUAL_HANGOUT,
            weatherSuitability = WeatherCondition.COOL,
            trendExplanation = "Workwear revival reinterpreted with tailored Japanese street proportions and clean hardware.",
            stylingExplanation = "Cropped jacket length visually elongates leg line without sacrificing warmth or rugged texture.",
            trendScore = 0.95f
        ),
        Outfit(
            id = "outfit_korean_fluid_tailoring",
            name = "Seoul Minimal Fluid Tailoring",
            category = StylePreferenceCategory.KOREAN_INSPIRED,
            garments = listOf(garments[0], garments[4]),
            colors = listOf("#18181B", "#E2E8F0", "#3F3F46"),
            shoes = "Polished leather square-toe mule loafers or minimalist sleek Derby",
            accessories = "Slim silver cuff bracelet, leather shoulder crossbody sling",
            occasion = Occasion.DATE,
            weatherSuitability = WeatherCondition.WARM,
            trendExplanation = "Subtle quiet-luxury aesthetics meeting K-drama minimalism through fluid drape and spotless hems.",
            stylingExplanation = "Tucking the structured crew into high-rise double pleats elevates the waistline seamlessly.",
            trendScore = 0.96f
        ),
        Outfit(
            id = "outfit_old_money_quiet_knit",
            name = "Coastal Quiet Luxury",
            category = StylePreferenceCategory.OLD_MONEY,
            garments = listOf(garments[3], garments[4]),
            colors = listOf("#E5E0D8", "#2C3E50", "#78716C"),
            shoes = "Suede Belgian loafers or clean white calfskin tennis sneakers",
            accessories = "Woven leather belt, vintage gold bezel dress watch",
            occasion = Occasion.DATE,
            weatherSuitability = WeatherCondition.COOL,
            trendExplanation = "Understated high-craft knits with zero loud branding, focusing on tactile luxury and immaculate drape.",
            stylingExplanation = "The half-zip neckline creates a flattering V-frame that elongates the neck and frames the chin cleanly.",
            trendScore = 0.93f
        ),
        Outfit(
            id = "outfit_heavy_oversized_lounge",
            name = "Heavyweight Tonal Oversized",
            category = StylePreferenceCategory.OVERSIZED,
            garments = listOf(garments[5], garments[1]),
            colors = listOf("#3B2F2F", "#5C5248", "#EFECE6"),
            shoes = "Chunky trail mule slip-ons or foam runners",
            accessories = "Beanie rolled above ears, padded ballistic nylon sling",
            occasion = Occasion.COLLEGE,
            weatherSuitability = WeatherCondition.COLD,
            trendExplanation = "High-GSM structural streetwear replacing flimsy fast-fashion fleeces with architectural drape.",
            stylingExplanation = "The double-layer structured hood acts as a stand-alone collar framing the jawline comfortably.",
            trendScore = 0.94f
        ),
        Outfit(
            id = "outfit_y2k_cyber_statement",
            name = "Neo-Y2K Cyber Street",
            category = StylePreferenceCategory.Y2K,
            garments = listOf(garments[0], garments[1]),
            colors = listOf("#0F172A", "#64748B", "#38BDF8"),
            shoes = "Metallic chrome accent technical runners",
            accessories = "Wraparound rimless sport sunglasses, modular carabiner clip keychain",
            occasion = Occasion.PARTY,
            weatherSuitability = WeatherCondition.WARM,
            trendExplanation = "Early 2000s cyber aesthetics modernized with premium Japanese denim and functional tech accessories.",
            stylingExplanation = "Bold footwear grounding wide-leg hems gives a futuristic, dynamic street presence.",
            trendScore = 0.94f
        ),
        Outfit(
            id = "outfit_smart_casual_clean",
            name = "Architectural Smart Casual",
            category = StylePreferenceCategory.SMART_CASUAL,
            garments = listOf(garments[3], garments[4]),
            colors = listOf("#1E293B", "#CBD5E1", "#475569"),
            shoes = "Minimalist Italian leather low-top sneakers in chalk white",
            accessories = "Minimalist leather laptop portfolio, silver signet ring",
            occasion = Occasion.INTERVIEW,
            weatherSuitability = WeatherCondition.COOL,
            trendExplanation = "Modern workplace dressing favoring sharp unstructured tailoring over stiff traditional suiting.",
            stylingExplanation = "Pleated trousers combined with tactile knitwear communicate refined confidence without rigidity.",
            trendScore = 0.91f
        ),
        Outfit(
            id = "outfit_techwear_rain_shield",
            name = "Modular Technical Shell",
            category = StylePreferenceCategory.TECHWEAR,
            garments = listOf(garments[0], garments[2], garments[1]),
            colors = listOf("#090A0C", "#262930", "#3E4451"),
            shoes = "Gore-Tex waterproof technical trail runners",
            accessories = "Waterproof crossbody chest rig, magnetic Fidlock key strap",
            occasion = Occasion.CASUAL_HANGOUT,
            weatherSuitability = WeatherCondition.RAINY,
            trendExplanation = "Gorpcore functionality paired with modern urban monochrome silhouette.",
            stylingExplanation = "Weatherproof articulated paneling provides full rain barrier while maintaining sleek lines.",
            trendScore = 0.92f
        ),
        Outfit(
            id = "outfit_skate_heavy_canvas",
            name = "Durable Skate & Workwear",
            category = StylePreferenceCategory.SKATE,
            garments = listOf(garments[0], garments[1]),
            colors = listOf("#44403C", "#D6D3D1", "#1C1917"),
            shoes = "Suede low-top skate shoes with contrast white vulcanized foxing",
            accessories = "Heavy-gauge wallet chain, embroidered corduroy 6-panel cap",
            occasion = Occasion.EVERYDAY,
            weatherSuitability = WeatherCondition.WARM,
            trendExplanation = "90s skate authenticity with heavyweight raw textiles built for daily wear.",
            stylingExplanation = "Relaxed carpenter cut prevents constriction around thighs while stacking naturally over skate sneakers.",
            trendScore = 0.90f
        ),
        Outfit(
            id = "outfit_summer_resort_minimal",
            name = "Breezy Vacation Linen Layer",
            category = StylePreferenceCategory.CASUAL,
            garments = listOf(garments[0]),
            colors = listOf("#F8FAFC", "#E2E8F0", "#38BDF8"),
            shoes = "Braided leather huarache sandals or canvas espadrilles",
            accessories = "Tortoiseshell acetate sunglasses, woven raffia tote",
            occasion = Occasion.VACATION,
            weatherSuitability = WeatherCondition.HOT,
            trendExplanation = "Resort leisure tailoring embracing open breathable textures and ocean sunlight tones.",
            stylingExplanation = "Airy cut allows maximum air circulation while looking effortlessly put-together.",
            trendScore = 0.91f
        )
    )
}
