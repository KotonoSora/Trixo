package com.kotonosora.tictactoe

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
import com.kotonosora.tictactoe.audio.LocalSoundManager
import com.kotonosora.tictactoe.audio.SoundManager
import com.kotonosora.tictactoe.di.LocalAppContainer
import com.kotonosora.tictactoe.ui.MainViewModel
import com.kotonosora.tictactoe.ui.TrixoApp
import com.kotonosora.tictactoe.ui.theme.TrixoTheme
import com.kotonosora.tictactoe.ui.viewmodels.TrixoViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as TrixoApplication).container

        viewModel = ViewModelProvider(
            this,
            TrixoViewModelFactory(container)
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

            CompositionLocalProvider(
                LocalSoundManager provides soundManager,
                LocalAppContainer provides container
            ) {
                TrixoTheme {
                    TrixoApp(viewModel = viewModel)
                }
            }
        }
    }
}
