package com.kotonosora.blitzxo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotonosora.blitzxo.di.LocalAppContainer
import com.kotonosora.blitzxo.domain.GameEvent
import com.kotonosora.blitzxo.domain.GameViewModel
import com.kotonosora.blitzxo.ui.navigation.TrixoDestinations
import com.kotonosora.blitzxo.ui.screens.CoinShopScreen
import com.kotonosora.blitzxo.ui.screens.DailyChallengesScreen
import com.kotonosora.blitzxo.ui.screens.GameModeScreen
import com.kotonosora.blitzxo.ui.screens.GameplayScreen
import com.kotonosora.blitzxo.ui.screens.HomeScreen
import com.kotonosora.blitzxo.ui.screens.LeaderboardScreen
import com.kotonosora.blitzxo.ui.screens.ProgressScreen
import com.kotonosora.blitzxo.ui.screens.ResultScreen
import com.kotonosora.blitzxo.ui.screens.SettingsScreen
import com.kotonosora.blitzxo.ui.viewmodels.DailyChallengesViewModel
import com.kotonosora.blitzxo.ui.viewmodels.TrixoViewModelFactory

@Composable
fun TrixoApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val container = LocalAppContainer.current
    val factory = remember(container) { TrixoViewModelFactory(container) }

    // Shared GameViewModel scoped to TrixoApp lifecycle
    val gameViewModel: GameViewModel = viewModel(factory = factory)
    val dailyChallengesViewModel: DailyChallengesViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = TrixoDestinations.HOME,
        modifier = modifier
    ) {
        composable(TrixoDestinations.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToGameMode = { navController.navigate(TrixoDestinations.GAME_MODE) },
                onNavigateToCoinShop = { navController.navigate(TrixoDestinations.COIN_SHOP) },
                onNavigateToProgress = { navController.navigate(TrixoDestinations.PROGRESS) },
                onNavigateToSettings = { navController.navigate(TrixoDestinations.SETTINGS) },
                onNavigateToDailyChallenges = { navController.navigate(TrixoDestinations.DAILY_CHALLENGES) },
                onNavigateToLeaderboard = { navController.navigate(TrixoDestinations.LEADERBOARD) }
            )
        }

        composable(TrixoDestinations.GAME_MODE) {
            GameModeScreen(
                gameViewModel = gameViewModel,
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGameplay = { navController.navigate(TrixoDestinations.GAMEPLAY) }
            )
        }

        composable(TrixoDestinations.GAMEPLAY) {
            GameplayScreen(
                gameViewModel = gameViewModel,
                mainViewModel = viewModel,
                dailyChallengesViewModel = dailyChallengesViewModel,
                onNavigateToResult = {
                    navController.navigate(TrixoDestinations.RESULT) {
                        popUpTo(TrixoDestinations.GAME_MODE) { inclusive = false }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(TrixoDestinations.RESULT) {
            ResultScreen(
                gameViewModel = gameViewModel,
                mainViewModel = viewModel,
                onNavigateHome = {
                    navController.navigate(TrixoDestinations.HOME) {
                        popUpTo(TrixoDestinations.HOME) { inclusive = true }
                    }
                },
                onNavigateToGameMode = {
                    navController.navigate(TrixoDestinations.GAME_MODE) {
                        popUpTo(TrixoDestinations.HOME) { inclusive = false }
                    }
                },
                onNavigateToNextLevel = { nextDifficulty ->
                    gameViewModel.onEvent(GameEvent.ResetGame(nextDifficulty, isPvP = false))
                    navController.navigate(TrixoDestinations.GAMEPLAY) {
                        popUpTo(TrixoDestinations.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(TrixoDestinations.COIN_SHOP) {
            CoinShopScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(TrixoDestinations.PROGRESS) {
            ProgressScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(TrixoDestinations.SETTINGS) {
            SettingsScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(TrixoDestinations.DAILY_CHALLENGES) {
            DailyChallengesScreen(
                mainViewModel = viewModel,
                dailyChallengesViewModel = dailyChallengesViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(TrixoDestinations.LEADERBOARD) {
            LeaderboardScreen(
                mainViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
