package com.linguaflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.linguaflow.app.navigation.LinguaFlowNavGraph
import com.linguaflow.app.theme.LinguaFlowTheme
import com.linguaflow.app.utils.PreferencesManager

class MainActivity : ComponentActivity() {

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        preferencesManager = PreferencesManager(applicationContext)

        setContent {
            val darkMode by preferencesManager.darkModeFlow.collectAsState(initial = false)

            LinguaFlowTheme(darkTheme = darkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LinguaFlowNavGraph()
                }
            }
        }
    }
}
