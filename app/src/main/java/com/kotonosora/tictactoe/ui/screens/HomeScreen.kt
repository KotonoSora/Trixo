package com.kotonosora.tictactoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.kotonosora.tictactoe.ui.MainViewModel
import com.kotonosora.tictactoe.ui.components.NeonButton
import com.kotonosora.tictactoe.ui.components.NeonTitle
import com.kotonosora.tictactoe.ui.components.MainTopBar
import com.kotonosora.tictactoe.ui.theme.NeonBlue
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonGreen
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.theme.AppTheme

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToGameMode: () -> Unit,
    onNavigateToCoinShop: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDailyChallenges: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPrefs by viewModel.userPreferences.collectAsState()

    HomeScreenContent(
        coins = userPrefs.coins,
        onNavigateToGameMode = onNavigateToGameMode,
        onNavigateToCoinShop = onNavigateToCoinShop,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToDailyChallenges = onNavigateToDailyChallenges,
        onNavigateToLeaderboard = onNavigateToLeaderboard,
        onNavigateToHelp = onNavigateToHelp,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    coins: Int,
    onNavigateToGameMode: () -> Unit,
    onNavigateToCoinShop: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDailyChallenges: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MainTopBar(
                coins = coins,
                onShopClick = onNavigateToCoinShop
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeonTitle("BLITZ", fontSize = 48)
                    NeonTitle("XO", fontSize = 48, color = NeonMagenta)
                }
            }

            // Menu Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NeonButton(
                    text = "PLAY GAME",
                    onClick = onNavigateToGameMode,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.PlayArrow,
                    height = 56,
                    fontSize = 14
                )

                NeonButton(
                    text = "DAILY CHALLENGE",
                    onClick = onNavigateToDailyChallenges,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonGreen,
                    icon = Icons.Rounded.Event,
                    height = 56,
                    fontSize = 14
                )

                NeonButton(
                    text = "LEADERBOARD",
                    onClick = onNavigateToLeaderboard,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonYellow,
                    icon = Icons.Rounded.EmojiEvents,
                    height = 56,
                    fontSize = 14
                )

                NeonButton(
                    text = "HELP",
                    onClick = onNavigateToHelp,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonCyan,
                    icon = Icons.AutoMirrored.Rounded.Help,
                    height = 56,
                    fontSize = 14
                )

                NeonButton(
                    text = "SKIN/POWER-UP SHOP",
                    onClick = onNavigateToCoinShop,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonMagenta,
                    icon = Icons.Rounded.ShoppingCart,
                    height = 56,
                    fontSize = 14
                )

                NeonButton(
                    text = "SETTING",
                    onClick = onNavigateToSettings,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonBlue,
                    icon = Icons.Rounded.Settings,
                    height = 56,
                    fontSize = 14
                )
            }

            // Bottom spacer for visual balance
            Spacer(modifier = Modifier.height(20.dp).navigationBarsPadding())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreenContent(
            coins = 300,
            onNavigateToGameMode = {},
            onNavigateToCoinShop = {},
            onNavigateToSettings = {},
            onNavigateToDailyChallenges = {},
            onNavigateToLeaderboard = {},
            onNavigateToHelp = {}
        )
    }
}
