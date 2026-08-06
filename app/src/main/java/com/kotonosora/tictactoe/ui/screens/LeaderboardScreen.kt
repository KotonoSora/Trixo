package com.kotonosora.tictactoe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.domain.model.GameHistory
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.components.NeonTitle
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.viewmodels.MainViewModel

@Composable
fun LeaderboardScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by mainViewModel.uiState.collectAsState()

    LeaderboardScreenContent(
        history = uiState.gameHistory,
        modifier = modifier
    )
}

@Composable
fun LeaderboardScreenContent(
    history: List<GameHistory>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        if (history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                NeonText("NO GAMES PLAYED YET", color = Color.Gray, fontSize = 14)
            }
        } else {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NeonText(
                    "DATE & TIME",
                    color = NeonCyan,
                    fontSize = 12,
                    modifier = Modifier.weight(1.5f)
                )
                NeonText(
                    "SCORE",
                    color = NeonCyan,
                    fontSize = 12,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                NeonText(
                    "REWARD",
                    color = NeonCyan,
                    fontSize = 12,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(history) { entry ->
                    LeaderboardItem(entry)
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(entry: GameHistory) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NeonCyan.copy(alpha = 0.05f))
            .border(1.dp, NeonCyan.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                val parts = entry.dateTime.split(" ")
                val date = parts.getOrNull(0) ?: ""
                val time = parts.getOrNull(1) ?: ""
                NeonText(
                    text = date,
                    color = Color.White,
                    fontSize = 11
                )
                if (time.isNotEmpty()) {
                    NeonText(
                        text = time,
                        color = Color.Gray,
                        fontSize = 9
                    )
                }
            }

            NeonText(
                text = entry.score.toString(),
                color = Color.White,
                fontSize = 14,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NeonTitle(
                    text = if (entry.rewardCoins > 0) "+${entry.rewardCoins}" else "${entry.rewardCoins}",
                    color = if (entry.rewardCoins > 0) NeonYellow else Color.Gray,
                    fontSize = 14,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    AppTheme {
        LeaderboardScreenContent(
            history = listOf(
                GameHistory(1, "2023-10-27 14:30", 50, 1250),
                GameHistory(2, "2023-10-26 10:15", 30, 1100),
                GameHistory(3, "2023-10-25 18:45", 25, 950)
            )
        )
    }
}
