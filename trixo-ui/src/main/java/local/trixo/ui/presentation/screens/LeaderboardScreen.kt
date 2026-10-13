package local.trixo.ui.presentation.screens

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import local.trixo.engine.domain.model.RunEndCause
import local.trixo.engine.domain.model.RunRecord
import local.trixo.ui.presentation.components.NeonText
import local.trixo.ui.presentation.components.NeonTitle
import local.trixo.ui.presentation.theme.AppTheme
import local.trixo.ui.presentation.theme.NeonCyan
import local.trixo.ui.presentation.theme.NeonYellow
import local.trixo.ui.presentation.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    history: List<RunRecord>,
    modifier: Modifier = Modifier
) {
    // Runs arrive ordered by recency; leaderboard shows best first.
    val sorted = remember(history) { history.sortedByDescending { it.score } }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        if (sorted.isEmpty()) {
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
                items(sorted) { entry ->
                    LeaderboardItem(entry)
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(entry: RunRecord) {
    val dateTime = remember(entry.endedAt) { formatDateTime(entry.endedAt) }
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
                val parts = dateTime.split(" ")
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
                    text = if (entry.reward > 0) "+${entry.reward}" else "${entry.reward}",
                    color = if (entry.reward > 0) NeonYellow else Color.Gray,
                    fontSize = 14,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    AppTheme {
        val now = System.currentTimeMillis()
        LeaderboardScreenContent(
            history = listOf(
                RunRecord(
                    mode = "EASY", level = 3, score = 1250, reward = 50,
                    durationMs = 60_000, pipesPassed = 5, endCause = RunEndCause.X_WINS,
                    startedAt = now - 60_000, endedAt = now
                ),
                RunRecord(
                    mode = "MEDIUM", level = 9, score = 1100, reward = 30,
                    durationMs = 120_000, pipesPassed = 20, endCause = RunEndCause.X_WINS,
                    startedAt = now - 120_000, endedAt = now - 86_400_000
                ),
                RunRecord(
                    mode = "HARD", level = 12, score = 950, reward = 25,
                    durationMs = 180_000, pipesPassed = 40, endCause = RunEndCause.DRAW,
                    startedAt = now - 180_000, endedAt = now - 2 * 86_400_000
                )
            )
        )
    }
}
