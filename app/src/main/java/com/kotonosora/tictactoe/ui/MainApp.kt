package com.kotonosora.tictactoe.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kotonosora.tictactoe.di.LocalAppContainer
import com.kotonosora.tictactoe.ui.navigation.AppDestinations
import com.kotonosora.tictactoe.ui.screens.CoinShopScreen
import com.kotonosora.tictactoe.ui.screens.DailyChallengesScreen
import com.kotonosora.tictactoe.ui.screens.GameModeScreen
import com.kotonosora.tictactoe.ui.screens.GameplayScreen
import com.kotonosora.tictactoe.ui.screens.HelpScreen
import com.kotonosora.tictactoe.ui.screens.HomeScreen
import com.kotonosora.tictactoe.ui.screens.LeaderboardScreen
import com.kotonosora.tictactoe.ui.screens.ProgressScreen
import com.kotonosora.tictactoe.ui.screens.ResultScreen
import com.kotonosora.tictactoe.ui.screens.SettingsScreen
import com.kotonosora.tictactoe.ui.viewmodels.AppViewModelFactory
import com.kotonosora.tictactoe.ui.viewmodels.DailyChallengesViewModel
import com.kotonosora.tictactoe.ui.viewmodels.GameEvent
import com.kotonosora.tictactoe.ui.viewmodels.GameViewModel
import com.kotonosora.tictactoe.ui.viewmodels.MainViewModel

@Composable
fun MainApp(
    viewModel: MainViewModel,
    gameViewModel: GameViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val container = LocalAppContainer.current
    val factory = remember(container) { AppViewModelFactory(container) }

    val dailyChallengesViewModel: DailyChallengesViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME,
        modifier = modifier.fillMaxSize()
    ) {
        composable(AppDestinations.HOME) {
            HomeScreen(
                onNavigateToGameMode = { navController.navigate(AppDestinations.GAME_MODE) },
                onNavigateToSettings = { navController.navigate(AppDestinations.SETTINGS) },
                onNavigateToDailyChallenges = { navController.navigate(AppDestinations.DAILY_CHALLENGES) },
                onNavigateToLeaderboard = { navController.navigate(AppDestinations.LEADERBOARD) },
                onNavigateToHelp = { navController.navigate(AppDestinations.HELP) }
            )
        }

        composable(AppDestinations.GAME_MODE) {
            GameModeScreen(
                gameViewModel = gameViewModel,
                onNavigateToGameplay = { navController.navigate(AppDestinations.GAMEPLAY) }
            )
        }

        composable(AppDestinations.GAMEPLAY) {
            GameplayScreen(
                gameViewModel = gameViewModel,
                mainViewModel = viewModel,
                dailyChallengesViewModel = dailyChallengesViewModel,
                onNavigateToResult = {
                    navController.navigate(AppDestinations.RESULT) {
                        popUpTo(AppDestinations.GAME_MODE) { inclusive = false }
                    }
                },
                onNavigateToShop = { navController.navigate(AppDestinations.COIN_SHOP) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.RESULT) {
            ResultScreen(
                gameViewModel = gameViewModel,
                onNavigateHome = {
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.HOME) { inclusive = true }
                    }
                },
                onNavigateToGameMode = {
                    navController.navigate(AppDestinations.GAME_MODE) {
                        popUpTo(AppDestinations.HOME) { inclusive = false }
                    }
                },
                onNavigateToNextLevel = { nextDifficulty ->
                    gameViewModel.onEvent(GameEvent.ResetGame(nextDifficulty, isPvP = false))
                    navController.navigate(AppDestinations.GAMEPLAY) {
                        popUpTo(AppDestinations.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(AppDestinations.COIN_SHOP) {
            CoinShopScreen(
                mainViewModel = viewModel
            )
        }

        composable(AppDestinations.PROGRESS) {
            ProgressScreen(
                mainViewModel = viewModel
            )
        }

        composable(AppDestinations.SETTINGS) {
            SettingsScreen(
                mainViewModel = viewModel
            )
        }

        composable(AppDestinations.DAILY_CHALLENGES) {
            DailyChallengesScreen(
                mainViewModel = viewModel,
                dailyChallengesViewModel = dailyChallengesViewModel,
                onNavigateToPlay = { navController.navigate(AppDestinations.GAME_MODE) }
            )
        }

        composable(AppDestinations.LEADERBOARD) {
            LeaderboardScreen(
                mainViewModel = viewModel
            )
        }

        composable(AppDestinations.HELP) {
            HelpScreen()
        }
    }
}
