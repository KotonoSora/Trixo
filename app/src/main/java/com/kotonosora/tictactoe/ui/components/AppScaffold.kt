package com.kotonosora.tictactoe.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kotonosora.tictactoe.ui.navigation.AppDestinations
import com.kotonosora.tictactoe.ui.viewmodels.GameResult
import com.kotonosora.tictactoe.ui.viewmodels.GameViewModel
import com.kotonosora.tictactoe.ui.viewmodels.MainViewModel
import com.kotonosora.tictactoe.utils.AppConstants

@Composable
fun AppScaffold(
    mainViewModel: MainViewModel,
    gameViewModel: GameViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val gameState by gameViewModel.gameState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            MainTopBar(
                coins = uiState.userPreferences.coins,
                title = when (currentRoute) {
                    AppDestinations.HOME -> null
                    AppDestinations.GAME_MODE -> null
                    AppDestinations.GAMEPLAY -> AppConstants.ScreenTitles.GAME
                    AppDestinations.RESULT -> when (gameState.result) {
                        GameResult.X_WINS -> AppConstants.ScreenTitles.VICTORY
                        GameResult.O_WINS -> AppConstants.ScreenTitles.GAME_OVER
                        GameResult.DRAW -> AppConstants.ScreenTitles.DRAW
                        else -> AppConstants.ScreenTitles.RESULT
                    }

                    AppDestinations.COIN_SHOP -> AppConstants.ScreenTitles.COIN_SHOP
                    AppDestinations.PROGRESS -> AppConstants.ScreenTitles.PROGRESS
                    AppDestinations.SETTINGS -> AppConstants.ScreenTitles.OPTIONS
                    AppDestinations.DAILY_CHALLENGES -> AppConstants.ScreenTitles.CHALLENGES
                    AppDestinations.LEADERBOARD -> AppConstants.ScreenTitles.HISTORY
                    AppDestinations.HELP -> AppConstants.ScreenTitles.HELP
                    else -> null
                },
                onBackClick = if (currentRoute != AppDestinations.HOME) {
                    { navController.popBackStack() }
                } else null,
                onShopClick = if (currentRoute != AppDestinations.COIN_SHOP) {
                    { navController.navigate(AppDestinations.COIN_SHOP) }
                } else null,
                showShopIcon = currentRoute == AppDestinations.HOME
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        content(innerPadding)
    }
}
