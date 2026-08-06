package com.kotonosora.tictactoe.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotonosora.tictactoe.di.LocalAppContainer
import com.kotonosora.tictactoe.domain.GameEvent
import com.kotonosora.tictactoe.domain.GameViewModel
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
import com.kotonosora.tictactoe.ui.viewmodels.DailyChallengesViewModel
import com.kotonosora.tictactoe.ui.viewmodels.AppViewModelFactory

@Composable
fun MainApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val container = LocalAppContainer.current
    val factory = remember(container) { AppViewModelFactory(container) }

    // Shared GameViewModel scoped to MainApp lifecycle
    val gameViewModel: GameViewModel = viewModel(factory = factory)
    val dailyChallengesViewModel: DailyChallengesViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME,
        modifier = modifier
    ) {
        composable(AppDestinations.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToGameMode = { navController.navigate(AppDestinations.GAME_MODE) },
                onNavigateToCoinShop = { navController.navigate(AppDestinations.COIN_SHOP) },
                onNavigateToSettings = { navController.navigate(AppDestinations.SETTINGS) },
                onNavigateToDailyChallenges = { navController.navigate(AppDestinations.DAILY_CHALLENGES) },
                onNavigateToLeaderboard = { navController.navigate(AppDestinations.LEADERBOARD) },
                onNavigateToHelp = { navController.navigate(AppDestinations.HELP) }
            )
        }

        composable(AppDestinations.GAME_MODE) {
            GameModeScreen(
                gameViewModel = gameViewModel,
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
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
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.RESULT) {
            ResultScreen(
                gameViewModel = gameViewModel,
                mainViewModel = viewModel,
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
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.PROGRESS) {
            ProgressScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.SETTINGS) {
            SettingsScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.DAILY_CHALLENGES) {
            DailyChallengesScreen(
                mainViewModel = viewModel,
                dailyChallengesViewModel = dailyChallengesViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.LEADERBOARD) {
            LeaderboardScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.HELP) {
            HelpScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
