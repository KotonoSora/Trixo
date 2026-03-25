package com.kotonosora.trixo.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotonosora.trixo.domain.GameViewModel
import com.kotonosora.trixo.ui.navigation.TrixoDestinations
import com.kotonosora.trixo.ui.screens.CoinShopScreen
import com.kotonosora.trixo.ui.screens.GameModeScreen
import com.kotonosora.trixo.ui.screens.GameplayScreen
import com.kotonosora.trixo.ui.screens.HomeScreen
import com.kotonosora.trixo.ui.screens.ProgressScreen
import com.kotonosora.trixo.ui.screens.ResultScreen
import com.kotonosora.trixo.ui.screens.SettingsScreen

@Composable
fun TrixoApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Shared GameViewModel scoped to TrixoApp lifecycle
    val gameViewModel: GameViewModel = viewModel()

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
                onNavigateToSettings = { navController.navigate(TrixoDestinations.SETTINGS) }
            )
        }
        
        composable(TrixoDestinations.GAME_MODE) {
            GameModeScreen(
                gameViewModel = gameViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGameplay = { navController.navigate(TrixoDestinations.GAMEPLAY) }
            )
        }
        
        composable(TrixoDestinations.GAMEPLAY) {
            GameplayScreen(
                gameViewModel = gameViewModel,
                mainViewModel = viewModel,
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
    }
}
