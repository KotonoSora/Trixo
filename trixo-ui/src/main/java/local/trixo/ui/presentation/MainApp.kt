package local.trixo.ui.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import local.trixo.ui.di.LocalAppContainer
import local.trixo.ui.presentation.navigation.AppDestinations
import local.trixo.ui.presentation.screens.CoinShopScreen
import local.trixo.ui.presentation.screens.DailyChallengesScreen
import local.trixo.ui.presentation.screens.GameModeScreen
import local.trixo.ui.presentation.screens.GameplayScreen
import local.trixo.ui.presentation.screens.HelpScreen
import local.trixo.ui.presentation.screens.HomeScreen
import local.trixo.ui.presentation.screens.LeaderboardScreen
import local.trixo.ui.presentation.screens.ProgressScreen
import local.trixo.ui.presentation.screens.ResultScreen
import local.trixo.ui.presentation.screens.SettingsScreen
import local.trixo.ui.di.AppViewModelFactory
import local.trixo.ui.presentation.viewmodel.DailyChallengesViewModel
import local.trixo.ui.presentation.viewmodel.GameEvent
import local.trixo.ui.presentation.viewmodel.GameViewModel
import local.trixo.ui.presentation.viewmodel.MainViewModel

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
