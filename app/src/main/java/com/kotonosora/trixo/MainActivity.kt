package com.kotonosora.trixo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import com.kotonosora.trixo.audio.LocalSoundManager
import com.kotonosora.trixo.audio.SoundManager
import com.kotonosora.trixo.data.UserPreferencesRepository
import com.kotonosora.trixo.data.dataStore
import com.kotonosora.trixo.ui.MainViewModel
import com.kotonosora.trixo.ui.MainViewModelFactory
import com.kotonosora.trixo.ui.TrixoApp
import com.kotonosora.trixo.ui.theme.TrixoTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize DataStore and Repository
        val repository = UserPreferencesRepository(applicationContext.dataStore)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        setContent {
            val soundManager = remember { SoundManager(applicationContext) }
            val userPrefs by viewModel.userPreferences.collectAsState()
            
            soundManager.soundEnabled = userPrefs.soundEnabled

            DisposableEffect(Unit) {
                onDispose {
                    soundManager.release()
                }
            }

            CompositionLocalProvider(LocalSoundManager provides soundManager) {
                TrixoTheme {
                    TrixoApp(viewModel = viewModel)
                }
            }
        }
    }
}
