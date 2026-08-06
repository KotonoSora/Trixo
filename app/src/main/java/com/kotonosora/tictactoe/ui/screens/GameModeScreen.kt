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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.ui.components.MainTopBar
import com.kotonosora.tictactoe.ui.components.NeonButton
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonGreen
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.viewmodels.Difficulty
import com.kotonosora.tictactoe.ui.viewmodels.GameEvent
import com.kotonosora.tictactoe.ui.viewmodels.GameViewModel
import com.kotonosora.tictactoe.ui.viewmodels.MainViewModel

@Composable
fun GameModeScreen(
    gameViewModel: GameViewModel,
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToGameplay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by mainViewModel.uiState.collectAsState()

    GameModeScreenContent(
        coins = uiState.userPreferences.coins,
        onNavigateBack = onNavigateBack,
        onDifficultySelected = { difficulty, isPvP ->
            gameViewModel.onEvent(GameEvent.ResetGame(difficulty, isPvP = isPvP))
            onNavigateToGameplay()
        },
        modifier = modifier
    )
}

@Composable
fun GameModeScreenContent(
    coins: Int,
    onNavigateBack: () -> Unit,
    onDifficultySelected: (Difficulty, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MainTopBar(
                coins = coins,
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
                    onClick = { onDifficultySelected(Difficulty.EASY, false) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton(
                    text = "MEDIUM (9x9)",
                    condition = "WIN: 4 IN A ROW",
                    color = NeonCyan,
                    onClick = { onDifficultySelected(Difficulty.MEDIUM, false) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton(
                    text = "HARD (12x12)",
                    condition = "WIN: 5 IN A ROW",
                    color = NeonYellow,
                    onClick = { onDifficultySelected(Difficulty.HARD, false) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton(
                    text = "INSANE (15x15)",
                    condition = "WIN: 6 IN A ROW",
                    color = NeonMagenta,
                    onClick = { onDifficultySelected(Difficulty.VERY_HARD, false) }
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
                    onClick = { onDifficultySelected(Difficulty.VERY_HARD, true) }
                )
            }

            Spacer(modifier = Modifier
                .height(24.dp)
                .navigationBarsPadding())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameModeScreenPreview() {
    AppTheme {
        GameModeScreenContent(
            coins = 300,
            onNavigateBack = {},
            onDifficultySelected = { _, _ -> }
        )
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

