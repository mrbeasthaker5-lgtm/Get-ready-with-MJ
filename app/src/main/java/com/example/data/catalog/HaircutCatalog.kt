package com.example.data.catalog

import com.example.data.model.FaceShape
import com.example.data.model.GenderStylePreference
import com.example.data.model.HairTexture
import com.example.data.model.Haircut
import com.example.data.model.MaintenanceLevel
import com.example.data.model.StylingDifficulty

object HaircutCatalog {
    val haircuts: List<Haircut> = listOf(
        Haircut(
            id = "textured_crop",
            name = "Textured Crop",
            category = "Crop",
            description = "Short blunt fringe with high textured crown and tapered or faded sides. Enhances jaw angularity while adding height.",
            minLength = "1.5 inches",
            maxLength = "3 inches",
            maintenanceLevel = MaintenanceLevel.MEDIUM,
            stylingDifficulty = StylingDifficulty.EASY,
            faceShapeScores = mapOf(
                FaceShape.OVAL to 0.95f,
                FaceShape.ROUND to 0.92f,
                FaceShape.SQUARE to 0.88f,
                FaceShape.HEART to 0.82f,
                FaceShape.OBLONG to 0.65f,
                FaceShape.DIAMOND to 0.86f,
                FaceShape.TRIANGLE to 0.80f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.90f,
                HairTexture.WAVY to 0.96f,
                HairTexture.CURLY to 0.88f,
                HairTexture.COILY to 0.84f,
                HairTexture.NOT_SURE to 0.88f
            ),
            trendScore = 0.95f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Ask for a mid drop fade with a textured point-cut blunt fringe on top (around 2 inches). Leave crown texture for styling powder. No heavy blunt weight lines."
        ),
        Haircut(
            id = "low_taper_fade",
            name = "Low Taper Fade",
            category = "Fade",
            description = "Subtle clean taper at the sideburns and nape, keeping fullness and natural flow throughout the bulk of the hair.",
            minLength = "2 inches",
            maxLength = "5 inches",
            maintenanceLevel = MaintenanceLevel.LOW,
            stylingDifficulty = StylingDifficulty.EASY,
            faceShapeScores = mapOf(
                FaceShape.OVAL to 0.96f,
                FaceShape.ROUND to 0.85f,
                FaceShape.SQUARE to 0.94f,
                FaceShape.HEART to 0.90f,
                FaceShape.OBLONG to 0.88f,
                FaceShape.DIAMOND to 0.92f,
                FaceShape.TRIANGLE to 0.89f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.92f,
                HairTexture.WAVY to 0.95f,
                HairTexture.CURLY to 0.97f,
                HairTexture.COILY to 0.94f,
                HairTexture.NOT_SURE to 0.92f
            ),
            trendScore = 0.98f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Clean low taper on the temples and neck. Keep bulk and weight around the parietals. Blend seamlessly into the natural length up top."
        ),
        Haircut(
            id = "two_block_curtains",
            name = "Two-Block Middle Part",
            category = "Fringe",
            description = "K-fashion staple featuring undercut sides and back with a voluminous center-parted drape that softly frames cheekbones.",
            minLength = "3.5 inches",
            maxLength = "6 inches",
            maintenanceLevel = MaintenanceLevel.MEDIUM,
            stylingDifficulty = StylingDifficulty.MEDIUM,
            faceShapeScores = mapOf(
                FaceShape.OVAL to 0.97f,
                FaceShape.HEART to 0.94f,
                FaceShape.DIAMOND to 0.93f,
                FaceShape.SQUARE to 0.82f,
                FaceShape.OBLONG to 0.86f,
                FaceShape.ROUND to 0.78f,
                FaceShape.TRIANGLE to 0.85f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.96f,
                HairTexture.WAVY to 0.92f,
                HairTexture.CURLY to 0.76f,
                HairTexture.COILY to 0.70f,
                HairTexture.NOT_SURE to 0.85f
            ),
            trendScore = 0.94f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.GIRL, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Clipper #3 undercut on sides and back up to the round of head. Disconnected layered top with center part curtain drape reaching cheekbones. Soft razor ends."
        ),
        Haircut(
            id = "modern_wolf_cut",
            name = "Modern Wolf Cut",
            category = "Layered",
            description = "Hybrid of the 70s shag and mullet featuring chopped face-framing layers, airy crown volume, and feathered graduated ends.",
            minLength = "4 inches",
            maxLength = "8 inches",
            maintenanceLevel = MaintenanceLevel.MEDIUM,
            stylingDifficulty = StylingDifficulty.MEDIUM,
            faceShapeScores = mapOf(
                FaceShape.OVAL to 0.94f,
                FaceShape.ROUND to 0.93f,
                FaceShape.HEART to 0.91f,
                FaceShape.DIAMOND to 0.95f,
                FaceShape.SQUARE to 0.90f,
                FaceShape.OBLONG to 0.84f,
                FaceShape.TRIANGLE to 0.88f
            ),
            hairTextureScores = mapOf(
                HairTexture.WAVY to 0.98f,
                HairTexture.CURLY to 0.94f,
                HairTexture.STRAIGHT to 0.82f,
                HairTexture.COILY to 0.88f,
                HairTexture.NOT_SURE to 0.88f
            ),
            trendScore = 0.96f,
            styleProfileCompatibility = listOf(GenderStylePreference.GIRL, GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Heavily layered wolf cut with short choppy layers on the crown for lift. Wispy face-framing curtain bangs blending into razored, wispy shoulder-grazing ends."
        ),
        Haircut(
            id = "messy_fringe_taper",
            name = "Messy French Fringe",
            category = "Fringe",
            description = "Forward-falling textured fringe with high-contrast movement. Excellent for shortening a high forehead and softening angular features.",
            minLength = "2.5 inches",
            maxLength = "4.5 inches",
            maintenanceLevel = MaintenanceLevel.LOW,
            stylingDifficulty = StylingDifficulty.EASY,
            faceShapeScores = mapOf(
                FaceShape.OBLONG to 0.98f,
                FaceShape.OVAL to 0.93f,
                FaceShape.DIAMOND to 0.91f,
                FaceShape.HEART to 0.89f,
                FaceShape.TRIANGLE to 0.85f,
                FaceShape.ROUND to 0.79f,
                FaceShape.SQUARE to 0.82f
            ),
            hairTextureScores = mapOf(
                HairTexture.WAVY to 0.97f,
                HairTexture.CURLY to 0.94f,
                HairTexture.STRAIGHT to 0.88f,
                HairTexture.COILY to 0.89f,
                HairTexture.NOT_SURE to 0.89f
            ),
            trendScore = 0.93f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Low taper fade on the sides. Top scissor cut forward at a slight angle with heavy internal texture and chip cutting for piecey fringe separation."
        ),
        Haircut(
            id = "bro_flow",
            name = "Bro Flow",
            category = "Flow",
            description = "Effortless medium-length swept-back look with natural wave and movement. Radiates casual luxury and relaxed vitality.",
            minLength = "4.5 inches",
            maxLength = "7 inches",
            maintenanceLevel = MaintenanceLevel.LOW,
            stylingDifficulty = StylingDifficulty.EASY,
            faceShapeScores = mapOf(
                FaceShape.SQUARE to 0.96f,
                FaceShape.OVAL to 0.95f,
                FaceShape.DIAMOND to 0.92f,
                FaceShape.TRIANGLE to 0.88f,
                FaceShape.HEART to 0.84f,
                FaceShape.ROUND to 0.77f,
                FaceShape.OBLONG to 0.81f
            ),
            hairTextureScores = mapOf(
                HairTexture.WAVY to 0.98f,
                HairTexture.STRAIGHT to 0.88f,
                HairTexture.CURLY to 0.90f,
                HairTexture.COILY to 0.78f,
                HairTexture.NOT_SURE to 0.88f
            ),
            trendScore = 0.91f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Scissor cut only. Graduation at the nape, maintaining ear tuck length. Heavy slide cut layering to encourage backward natural wave direction."
        ),
        Haircut(
            id = "textured_quiff",
            name = "Textured Modern Quiff",
            category = "Classic",
            description = "Vertical front lift that elongates the face and creates prominent structure. Clean tapered side blend.",
            minLength = "2.5 inches",
            maxLength = "4 inches",
            maintenanceLevel = MaintenanceLevel.HIGH,
            stylingDifficulty = StylingDifficulty.MEDIUM,
            faceShapeScores = mapOf(
                FaceShape.ROUND to 0.97f,
                FaceShape.SQUARE to 0.93f,
                FaceShape.OVAL to 0.94f,
                FaceShape.DIAMOND to 0.85f,
                FaceShape.HEART to 0.80f,
                FaceShape.OBLONG to 0.60f,
                FaceShape.TRIANGLE to 0.82f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.95f,
                HairTexture.WAVY to 0.92f,
                HairTexture.CURLY to 0.80f,
                HairTexture.COILY to 0.75f,
                HairTexture.NOT_SURE to 0.86f
            ),
            trendScore = 0.90f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Mid skin fade on back and sides. Leave 3.5 inches at the front fringe transitioning down to 2 inches at the crown. Keep density in the front hairline."
        ),
        Haircut(
            id = "butterfly_cut",
            name = "90s Butterfly Layers",
            category = "Layered",
            description = "Cascading voluminous layers that frame the face with airy bounce and curtain swoops, creating a stunning salon blowout effect.",
            minLength = "6 inches",
            maxLength = "14 inches",
            maintenanceLevel = MaintenanceLevel.HIGH,
            stylingDifficulty = StylingDifficulty.ADVANCED,
            faceShapeScores = mapOf(
                FaceShape.ROUND to 0.96f,
                FaceShape.SQUARE to 0.95f,
                FaceShape.OVAL to 0.96f,
                FaceShape.DIAMOND to 0.93f,
                FaceShape.HEART to 0.90f,
                FaceShape.OBLONG to 0.88f,
                FaceShape.TRIANGLE to 0.91f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.93f,
                HairTexture.WAVY to 0.97f,
                HairTexture.CURLY to 0.89f,
                HairTexture.COILY to 0.83f,
                HairTexture.NOT_SURE to 0.90f
            ),
            trendScore = 0.97f,
            styleProfileCompatibility = listOf(GenderStylePreference.GIRL, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Extreme cascading layers. Shortest top layer at chin height mimicking a faux bob, longest layers flowing down past collarbone with flicked ends."
        ),
        Haircut(
            id = "buzz_cut_lineup",
            name = "Geometric Buzz & Clean Line-up",
            category = "Buzz",
            description = "Ultra-minimalist short cut that highlights striking jawline definition, cheekbone structure, and facial symmetry.",
            minLength = "0.2 inches",
            maxLength = "0.5 inches",
            maintenanceLevel = MaintenanceLevel.LOW,
            stylingDifficulty = StylingDifficulty.EASY,
            faceShapeScores = mapOf(
                FaceShape.OVAL to 0.96f,
                FaceShape.SQUARE to 0.96f,
                FaceShape.DIAMOND to 0.92f,
                FaceShape.HEART to 0.82f,
                FaceShape.ROUND to 0.74f,
                FaceShape.OBLONG to 0.72f,
                FaceShape.TRIANGLE to 0.80f
            ),
            hairTextureScores = mapOf(
                HairTexture.COILY to 0.98f,
                HairTexture.CURLY to 0.95f,
                HairTexture.STRAIGHT to 0.91f,
                HairTexture.WAVY to 0.92f,
                HairTexture.NOT_SURE to 0.92f
            ),
            trendScore = 0.92f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Number 2 guard uniform on top with a crisp temple taper and sharp hairline shape-up. Finished with straight-razor detailing."
        ),
        Haircut(
            id = "curly_burst_fade",
            name = "Curly Burst Fade",
            category = "Fade",
            description = "Rounds out behind the ears highlighting natural coils and ringlets on top with tight, clean negative space around the ears.",
            minLength = "2.5 inches",
            maxLength = "5 inches",
            maintenanceLevel = MaintenanceLevel.MEDIUM,
            stylingDifficulty = StylingDifficulty.MEDIUM,
            faceShapeScores = mapOf(
                FaceShape.ROUND to 0.92f,
                FaceShape.OVAL to 0.95f,
                FaceShape.SQUARE to 0.93f,
                FaceShape.DIAMOND to 0.94f,
                FaceShape.HEART to 0.87f,
                FaceShape.OBLONG to 0.82f,
                FaceShape.TRIANGLE to 0.86f
            ),
            hairTextureScores = mapOf(
                HairTexture.CURLY to 0.99f,
                HairTexture.COILY to 0.98f,
                HairTexture.WAVY to 0.85f,
                HairTexture.STRAIGHT to 0.65f,
                HairTexture.NOT_SURE to 0.87f
            ),
            trendScore = 0.96f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.GIRL, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Burst fade circling around the ear into the neckline. Free-hand curl sculpting on top preserving moisture curl pattern and natural bounce."
        ),
        Haircut(
            id = "slick_back_undercut",
            name = "Polished Slick Back Undercut",
            category = "Classic",
            description = "Sophisticated, structured aesthetic with sleek sweep and sharp contrast. Highlights strong bone structure and chiseled jawlines.",
            minLength = "3.5 inches",
            maxLength = "5 inches",
            maintenanceLevel = MaintenanceLevel.MEDIUM,
            stylingDifficulty = StylingDifficulty.MEDIUM,
            faceShapeScores = mapOf(
                FaceShape.SQUARE to 0.95f,
                FaceShape.OVAL to 0.94f,
                FaceShape.DIAMOND to 0.91f,
                FaceShape.ROUND to 0.84f,
                FaceShape.HEART to 0.80f,
                FaceShape.OBLONG to 0.68f,
                FaceShape.TRIANGLE to 0.83f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.97f,
                HairTexture.WAVY to 0.90f,
                HairTexture.CURLY to 0.74f,
                HairTexture.COILY to 0.70f,
                HairTexture.NOT_SURE to 0.85f
            ),
            trendScore = 0.89f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "High disconnected undercut on the sides (#1 guard). Top layered to lie flat backward with shear over comb blending at the crown."
        ),
        Haircut(
            id = "modern_mullet_edgy",
            name = "Modern Shaggy Mullet",
            category = "Flow",
            description = "Gen-Z statement cut with short textured temples and top blending into a controlled, fluid neckline silhouette.",
            minLength = "3 inches",
            maxLength = "6.5 inches",
            maintenanceLevel = MaintenanceLevel.LOW,
            stylingDifficulty = StylingDifficulty.EASY,
            faceShapeScores = mapOf(
                FaceShape.DIAMOND to 0.95f,
                FaceShape.HEART to 0.94f,
                FaceShape.OVAL to 0.92f,
                FaceShape.SQUARE to 0.90f,
                FaceShape.TRIANGLE to 0.89f,
                FaceShape.OBLONG to 0.80f,
                FaceShape.ROUND to 0.78f
            ),
            hairTextureScores = mapOf(
                HairTexture.WAVY to 0.97f,
                HairTexture.CURLY to 0.95f,
                HairTexture.STRAIGHT to 0.84f,
                HairTexture.COILY to 0.90f,
                HairTexture.NOT_SURE to 0.89f
            ),
            trendScore = 0.95f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.GIRL, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Keep temple points soft or taper lightly. Heavily point-cut top with piecey crown. Preserve length flowing 2 inches past collar with razored softness."
        ),
        Haircut(
            id = "blunt_bob_framing",
            name = "French Chin Bob",
            category = "Crop",
            description = "Crisp, architectural chin-length baseline paired with subtle eyebrow-grazing micro bangs. Accentuates neck length and jawline definition.",
            minLength = "4 inches",
            maxLength = "7 inches",
            maintenanceLevel = MaintenanceLevel.HIGH,
            stylingDifficulty = StylingDifficulty.MEDIUM,
            faceShapeScores = mapOf(
                FaceShape.OVAL to 0.97f,
                FaceShape.HEART to 0.95f,
                FaceShape.OBLONG to 0.92f,
                FaceShape.DIAMOND to 0.90f,
                FaceShape.SQUARE to 0.81f,
                FaceShape.ROUND to 0.75f,
                FaceShape.TRIANGLE to 0.84f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.98f,
                HairTexture.WAVY to 0.91f,
                HairTexture.CURLY to 0.82f,
                HairTexture.COILY to 0.80f,
                HairTexture.NOT_SURE to 0.88f
            ),
            trendScore = 0.93f,
            styleProfileCompatibility = listOf(GenderStylePreference.GIRL, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Precision perimeter cut hitting exactly at the jaw angle. Subtle beveling under to promote natural inward roll. Eyebrow level wispy fringe."
        ),
        Haircut(
            id = "side_part_pompadour",
            name = "Executive Side Part Taper",
            category = "Classic",
            description = "Timeless gentleman cut with razor hard part and smooth contoured volume. Provides structured balance for rounded or heart silhouettes.",
            minLength = "2.5 inches",
            maxLength = "4 inches",
            maintenanceLevel = MaintenanceLevel.MEDIUM,
            stylingDifficulty = StylingDifficulty.MEDIUM,
            faceShapeScores = mapOf(
                FaceShape.ROUND to 0.96f,
                FaceShape.SQUARE to 0.94f,
                FaceShape.OVAL to 0.95f,
                FaceShape.HEART to 0.87f,
                FaceShape.DIAMOND to 0.86f,
                FaceShape.TRIANGLE to 0.83f,
                FaceShape.OBLONG to 0.70f
            ),
            hairTextureScores = mapOf(
                HairTexture.STRAIGHT to 0.97f,
                HairTexture.WAVY to 0.91f,
                HairTexture.CURLY to 0.72f,
                HairTexture.COILY to 0.70f,
                HairTexture.NOT_SURE to 0.85f
            ),
            trendScore = 0.86f,
            styleProfileCompatibility = listOf(GenderStylePreference.BOY, GenderStylePreference.UNSPECIFIED),
            barberInstructions = "Taper sides from #1 up to scissor length. Comb natural side part and clean line. Scissor cut top with graduation towards the back crown."
        )
    )
}
