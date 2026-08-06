package com.kotonosora.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.kotonosora.tictactoe.audio.LocalSoundManager
import com.kotonosora.tictactoe.audio.SoundManager
import com.kotonosora.tictactoe.di.LocalAppContainer
import com.kotonosora.tictactoe.ui.MainApp
import com.kotonosora.tictactoe.ui.components.AppScaffold
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.viewmodels.AppViewModelFactory
import com.kotonosora.tictactoe.ui.viewmodels.GameViewModel
import com.kotonosora.tictactoe.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as MainApplication).container

        viewModel = ViewModelProvider(
            this,
            AppViewModelFactory(container)
        )[MainViewModel::class.java]

        setContent {
            val soundManager = remember { SoundManager(applicationContext) }
            val uiState by viewModel.uiState.collectAsState()

            soundManager.soundEnabled = uiState.userPreferences.soundEnabled

            DisposableEffect(Unit) {
                onDispose {
                    soundManager.release()
                }
            }

            CompositionLocalProvider(
                LocalSoundManager provides soundManager,
                LocalAppContainer provides container
            ) {
                AppTheme {
                    val navController = rememberNavController()
                    val factory = remember(container) { AppViewModelFactory(container) }
                    val gameViewModel: GameViewModel = viewModel(factory = factory)

                    AppScaffold(
                        mainViewModel = viewModel,
                        gameViewModel = gameViewModel,
                        navController = navController
                    ) { innerPadding ->
                        MainApp(
                            viewModel = viewModel,
                            gameViewModel = gameViewModel,
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
