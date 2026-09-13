package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedLookDao {
    @Query("SELECT * FROM saved_looks ORDER BY timestamp DESC")
    fun getAllSavedLooks(): Flow<List<SavedLookEntity>>

    @Query("SELECT * FROM saved_looks WHERE lookType = :type ORDER BY timestamp DESC")
    fun getSavedLooksByType(type: String): Flow<List<SavedLookEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_looks WHERE referenceId = :refId LIMIT 1)")
    fun isLookSaved(refId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedLook(look: SavedLookEntity): Long

    @Query("DELETE FROM saved_looks WHERE referenceId = :refId")
    suspend fun deleteByReferenceId(refId: String)

    @Query("DELETE FROM saved_looks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM saved_looks")
    suspend fun clearAll()
}

@Dao
interface UserFeedbackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: UserFeedbackEntity): Long

    @Query("SELECT * FROM user_feedbacks ORDER BY timestamp DESC LIMIT 20")
    fun getRecentFeedbacks(): Flow<List<UserFeedbackEntity>>
}

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreferences(prefs: UserPreferencesEntity)
}
