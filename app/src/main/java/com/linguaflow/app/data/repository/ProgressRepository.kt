package com.linguaflow.app.data.repository

import com.linguaflow.app.data.database.dao.AchievementDao
import com.linguaflow.app.data.database.dao.ProgressDao
import com.linguaflow.app.data.database.dao.StreakDao
import com.linguaflow.app.data.database.entity.AchievementEntity
import com.linguaflow.app.data.database.entity.ProgressEntity
import com.linguaflow.app.data.database.entity.StreakEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProgressRepository(
    private val progressDao: ProgressDao,
    private val streakDao: StreakDao,
    private val achievementDao: AchievementDao
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun observeRecentProgress(days: Int = 30): Flow<List<ProgressEntity>> =
        progressDao.observeRecent(days)

    fun observeStreak(): Flow<StreakEntity?> = streakDao.observe()

    fun observeAchievements() = achievementDao.observeAll()

    suspend fun logStudySession(minutes: Int, wordsLearned: Int, xpEarned: Int, lessonCompleted: Boolean) {
        val today = dateFormat.format(Date())
        val existing = progressDao.getForDate(today)
        val updated = ProgressEntity(
            date = today,
            minutesStudied = (existing?.minutesStudied ?: 0) + minutes,
            wordsLearned = (existing?.wordsLearned ?: 0) + wordsLearned,
            lessonsCompleted = (existing?.lessonsCompleted ?: 0) + if (lessonCompleted) 1 else 0,
            xpEarned = (existing?.xpEarned ?: 0) + xpEarned
        )
        progressDao.upsert(updated)
    }

    suspend fun unlockAchievement(id: String) = achievementDao.unlock(id, System.currentTimeMillis())

    suspend fun seedAchievements(achievements: List<AchievementEntity>) = achievementDao.insertAll(achievements)

    // Add this inside the ProgressRepository class
    // Temporary fix to get the app running

    suspend fun getTotalXp(): Int {
        return progressDao.getTotalXp() ?: 0
    }
}
