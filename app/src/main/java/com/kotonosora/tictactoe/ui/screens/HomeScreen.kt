package com.kotonosora.tictactoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.ui.components.NeonButton
import com.kotonosora.tictactoe.ui.components.NeonTitle
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.theme.NeonBlue
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonGreen
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonYellow

@Composable
fun HomeScreen(
    onNavigateToGameMode: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDailyChallenges: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeScreenContent(
        onNavigateToGameMode = onNavigateToGameMode,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToDailyChallenges = onNavigateToDailyChallenges,
        onNavigateToLeaderboard = onNavigateToLeaderboard,
        onNavigateToHelp = onNavigateToHelp,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    onNavigateToGameMode: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDailyChallenges: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
                NeonTitle("BLITZ", fontSize = 42)
                NeonTitle("XO", fontSize = 42, color = NeonMagenta)
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
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreenContent(
            onNavigateToGameMode = {},
            onNavigateToSettings = {},
            onNavigateToDailyChallenges = {},
            onNavigateToLeaderboard = {},
            onNavigateToHelp = {}
        )
    }
}
