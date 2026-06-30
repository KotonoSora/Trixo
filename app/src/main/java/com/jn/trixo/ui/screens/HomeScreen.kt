package com.jn.trixo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jn.trixo.ui.MainViewModel
import com.jn.trixo.ui.components.NeonButton
import com.jn.trixo.ui.components.NeonTitle
import com.jn.trixo.ui.components.TrixoTopBar
import com.jn.trixo.ui.theme.NeonBlue
import com.jn.trixo.ui.theme.NeonGreen
import com.jn.trixo.ui.theme.NeonMagenta
import com.jn.trixo.ui.theme.NeonYellow

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToGameMode: () -> Unit,
    onNavigateToCoinShop: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDailyChallenges: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPrefs by viewModel.userPreferences.collectAsState()

    Scaffold(
        topBar = {
            TrixoTopBar(
                coins = userPrefs.coins,
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
                    NeonTitle("TRIX", fontSize = 48)
                    NeonTitle("O", fontSize = 48, color = NeonMagenta)
                }
            }

            // Menu Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NeonButton(
                    text = "PLAY NOW",
                    onClick = onNavigateToGameMode,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.PlayArrow,
                    height = 64,
                    fontSize = 16
                )

                NeonButton(
                    text = "CHALLENGES",
                    onClick = onNavigateToDailyChallenges,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonGreen,
                    icon = Icons.Rounded.Event,
                    height = 64,
                    fontSize = 16
                )

                NeonButton(
                    text = "LEADERBOARD",
                    onClick = onNavigateToLeaderboard,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonYellow,
                    icon = Icons.Rounded.EmojiEvents,
                    height = 64,
                    fontSize = 16
                )

                NeonButton(
                    text = "STATS",
                    onClick = onNavigateToProgress,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonMagenta,
                    icon = Icons.Rounded.Timeline,
                    height = 64,
                    fontSize = 16
                )

                NeonButton(
                    text = "SETTINGS",
                    onClick = onNavigateToSettings,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonBlue,
                    icon = Icons.Rounded.Settings,
                    height = 64,
                    fontSize = 16
                )
            }

            // Bottom spacer for visual balance
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
