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
import local.trixo.ui.presentation.MainApp
import local.trixo.ui.infrastructure.audio.LocalSoundManager
import local.trixo.ui.infrastructure.audio.SoundManager
import local.trixo.ui.presentation.components.AppScaffold
import local.trixo.ui.di.AppContainerHost
import local.trixo.ui.di.AppViewModelFactory
import local.trixo.ui.di.LocalAppContainer
import local.trixo.ui.presentation.theme.AppTheme
import local.trixo.ui.presentation.viewmodel.GameViewModel
import local.trixo.ui.presentation.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as AppContainerHost).container

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
