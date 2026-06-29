package com.jn.trixo

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
import com.jn.trixo.audio.LocalSoundManager
import com.jn.trixo.audio.SoundManager
import com.jn.trixo.data.UserPreferencesRepository
import com.jn.trixo.data.dataStore
import com.jn.trixo.ui.MainViewModel
import com.jn.trixo.ui.MainViewModelFactory
import com.jn.trixo.ui.TrixoApp
import com.jn.trixo.ui.theme.TrixoTheme

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
