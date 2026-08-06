package com.kotonosora.tictactoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.PrecisionManufacturing
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.domain.Difficulty
import com.kotonosora.tictactoe.domain.GameEvent
import com.kotonosora.tictactoe.domain.GameViewModel
import com.kotonosora.tictactoe.ui.MainViewModel
import com.kotonosora.tictactoe.ui.components.NeonButton
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.components.TrixoTopBar
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonGreen
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonYellow

@Composable
fun GameModeScreen(
    gameViewModel: GameViewModel,
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToGameplay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPreferences by mainViewModel.userPreferences.collectAsState()

    Scaffold(
        topBar = {
            TrixoTopBar(
                coins = userPreferences.coins,
                onBackClick = onNavigateBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Section Title: VS AI
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.PrecisionManufacturing,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NeonText(
                        text = "VS AI (WITH POWERS)",
                        color = NeonCyan,
                        fontSize = 16,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                DifficultyButton(
                    text = "EASY (3x3)",
                    condition = "WIN: 3 IN A ROW",
                    color = NeonGreen,
                    onClick = {
                        gameViewModel.onEvent(GameEvent.ResetGame(Difficulty.EASY, isPvP = false))
                        onNavigateToGameplay()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton(
                    text = "MEDIUM (9x9)",
                    condition = "WIN: 4 IN A ROW",
                    color = NeonCyan,
                    onClick = {
                        gameViewModel.onEvent(GameEvent.ResetGame(Difficulty.MEDIUM, isPvP = false))
                        onNavigateToGameplay()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton(
                    text = "HARD (12x12)",
                    condition = "WIN: 5 IN A ROW",
                    color = NeonYellow,
                    onClick = {
                        gameViewModel.onEvent(GameEvent.ResetGame(Difficulty.HARD, isPvP = false))
                        onNavigateToGameplay()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton(
                    text = "INSANE (15x15)",
                    condition = "WIN: 6 IN A ROW",
                    color = NeonMagenta,
                    onClick = {
                        gameViewModel.onEvent(
                            GameEvent.ResetGame(
                                Difficulty.VERY_HARD,
                                isPvP = false
                            )
                        )
                        onNavigateToGameplay()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section Title: LOCAL PvP
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Group,
                        contentDescription = null,
                        tint = NeonMagenta,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NeonText(
                        text = "LOCAL 2 PLAYERS (NO POWERS)",
                        color = NeonMagenta,
                        fontSize = 16,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                DifficultyButton(
                    text = "LOCAL BATTLE (15x15)",
                    condition = "WIN: 6 IN A ROW",
                    color = NeonMagenta,
                    onClick = {
                        gameViewModel.onEvent(
                            GameEvent.ResetGame(
                                Difficulty.VERY_HARD,
                                isPvP = true
                            )
                        )
                        onNavigateToGameplay()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DifficultyButton(
    text: String,
    condition: String,
    color: Color,
    onClick: () -> Unit
) {
    NeonButton(
        text = "$text\n$condition",
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = color,
        height = 80,
        fontSize = 14
    )
}

