package com.linguaflow.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.linguaflow.app.data.SeedData
import com.linguaflow.app.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
/**
 * Application entry point.
 * Initializes Firebase and seeds demo course content on first launch.
 */
class LinguaFlowApp : Application() {

    private val appScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val container = AppContainer.get(this)
        appScope.launch {
            // Standard Kotlin extension function syntax: flow.first()
            val alreadySeeded = container.preferencesManager.seedDoneFlow.first()

            if (!alreadySeeded) {
                SeedData.seedIfNeeded(container)
                container.preferencesManager.setSeedDone(true)
            }
        }
    }
}
