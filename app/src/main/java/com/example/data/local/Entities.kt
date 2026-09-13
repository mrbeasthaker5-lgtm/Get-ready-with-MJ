package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_looks")
data class SavedLookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lookType: String, // "HAIRCUT" or "OUTFIT"
    val referenceId: String,
    val title: String,
    val subtitle: String,
    val tags: String,
    val details: String,
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_feedbacks")
data class UserFeedbackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recommendationType: String,
    val rating: String, // "GREAT", "OKAY", "NOT_USEFUL"
    val feedbackText: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val id: Int = 1,
    val genderPreference: String = "UNSPECIFIED",
    val heightCm: Float = 175f,
    val proportion: String = "BALANCED",
    val hairTexture: String = "NOT_SURE",
    val preferredStyles: String = "STREETWEAR,CLEAN_FIT",
    val darkTheme: Boolean = true,
    val notifications: Boolean = true
)
