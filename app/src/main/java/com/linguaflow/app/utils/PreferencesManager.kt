package com.linguaflow.app.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "linguaflow_prefs")

/**
 * Wraps Jetpack DataStore for lightweight app preferences:
 * dark mode, onboarding completion, daily goal minutes, target/native language.
 */
class PreferencesManager(private val context: Context) {

    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val DAILY_GOAL_MINUTES = intPreferencesKey("daily_goal_minutes")
        val TARGET_LANGUAGE = stringPreferencesKey("target_language")
        val NATIVE_LANGUAGE = stringPreferencesKey("native_language")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val SOUND_EFFECTS_ENABLED = booleanPreferencesKey("sound_effects_enabled")
        val SEED_DONE = booleanPreferencesKey("seed_done")


        // List of languages the app supports
        val SUPPORTED_LANGUAGES = listOf("Spanish", "French", "German", "Italian", "Japanese")
    }

    val seedDoneFlow: Flow<Boolean> =
        context.dataStore.data.map { it[SEED_DONE] ?: false }

    suspend fun setSeedDone(done: Boolean) {
        context.dataStore.edit { it[SEED_DONE] = done }
    }

    val darkModeFlow: Flow<Boolean> =
        context.dataStore.data.map { it[DARK_MODE] ?: false }


    val targetLanguageFlow: Flow<String> =
        context.dataStore.data.map { it[TARGET_LANGUAGE] ?: "Spanish" }

    val onboardingDoneFlow: Flow<Boolean> =
        context.dataStore.data.map { it[ONBOARDING_DONE] ?: false }

    val dailyGoalFlow: Flow<Int> =
        context.dataStore.data.map { it[DAILY_GOAL_MINUTES] ?: 10 }



    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun setOnboardingDone(done: Boolean) {
        context.dataStore.edit { it[ONBOARDING_DONE] = done }
    }

    suspend fun setDailyGoal(minutes: Int) {
        context.dataStore.edit { it[DAILY_GOAL_MINUTES] = minutes }
    }

    suspend fun setTargetLanguage(language: String) {
        context.dataStore.edit { it[TARGET_LANGUAGE] = language }
    }

    suspend fun setNativeLanguage(language: String) {
        context.dataStore.edit { it[NATIVE_LANGUAGE] = language }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }
}
