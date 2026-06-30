package com.jn.trixo.ui.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.jn.trixo.data.history.GameHistoryEntry
import com.jn.trixo.ui.MainViewModel
import com.jn.trixo.ui.components.NeonText
import com.jn.trixo.ui.components.NeonTitle
import com.jn.trixo.ui.components.TrixoTopBar
import com.jn.trixo.ui.theme.NeonCyan
import com.jn.trixo.ui.theme.NeonYellow
import com.jn.trixo.ui.theme.TrixoTheme

@Composable
fun LeaderboardScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPrefs by mainViewModel.userPreferences.collectAsState()
    val history by mainViewModel.gameHistory.collectAsState()

    LeaderboardScreenContent(
        coins = userPrefs.coins,
        history = history,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun LeaderboardScreenContent(
    coins: Int,
    history: List<GameHistoryEntry>,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TrixoTopBar(
                coins = coins,
                title = "MY HISTORY",
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(history) { entry ->
                        LeaderboardItem(entry)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(entry: GameHistoryEntry) {
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
                NeonText(
                    text = entry.dateTime.split(" ")[0],
                    color = Color.White,
                    fontSize = 11
                )
                NeonText(
                    text = entry.dateTime.split(" ")[1],
                    color = Color.Gray,
                    fontSize = 9
                )
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
    TrixoTheme {
        LeaderboardScreenContent(
            coins = 250,
            history = listOf(
                GameHistoryEntry(1, "2023-10-27 14:30", 50, 1250),
                GameHistoryEntry(2, "2023-10-26 10:15", 30, 1100),
                GameHistoryEntry(3, "2023-10-25 18:45", 25, 950)
            ),
            onNavigateBack = {}
        )
    }
}
