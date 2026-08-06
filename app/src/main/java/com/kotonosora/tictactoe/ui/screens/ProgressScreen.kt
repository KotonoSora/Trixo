package com.kotonosora.tictactoe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.VideogameAsset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.ui.MainViewModel
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.components.NeonTitle
import com.kotonosora.tictactoe.ui.components.MainTopBar
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.theme.AppTheme

@Composable
fun ProgressScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPreferences by mainViewModel.userPreferences.collectAsState()

    ProgressScreenContent(
        coins = userPreferences.coins,
        highScore = userPreferences.highScore,
        gamesPlayed = userPreferences.gamesPlayed,
        gamesWon = userPreferences.gamesWon,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun ProgressScreenContent(
    coins: Int,
    highScore: Int,
    gamesPlayed: Int,
    gamesWon: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val winRate = if (gamesPlayed > 0) gamesWon.toFloat() / gamesPlayed else 0f

    Scaffold(
        topBar = {
            MainTopBar(
                coins = coins,
                title = "STATS",
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
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // High Score Neon Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(NeonYellow.copy(alpha = 0.05f))
                    .border(2.dp, NeonYellow, RoundedCornerShape(24.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NeonText("HIGH SCORE", color = NeonYellow, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    NeonTitle("$highScore", fontSize = 48, color = NeonYellow)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Win Rate Neon Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(NeonCyan.copy(alpha = 0.05f))
                    .border(2.dp, NeonCyan, RoundedCornerShape(24.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NeonText("WIN RATE", color = NeonCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { winRate },
                            modifier = Modifier.size(120.dp),
                            color = NeonCyan,
                            strokeWidth = 8.dp,
                            trackColor = NeonCyan.copy(alpha = 0.1f)
                        )
                        NeonTitle("${(winRate * 100).toInt()}%", fontSize = 24)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Played Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonMagenta.copy(alpha = 0.05f))
                        .border(1.dp, NeonMagenta, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Rounded.VideogameAsset,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = NeonMagenta
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        NeonText("PLAYED", color = NeonMagenta, fontSize = 12)
                        Spacer(modifier = Modifier.height(8.dp))
                        NeonTitle("$gamesPlayed", fontSize = 20, color = NeonMagenta)
                    }
                }

                // Won Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonYellow.copy(alpha = 0.05f))
                        .border(1.dp, NeonYellow, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Rounded.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = NeonYellow
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        NeonText("WON", color = NeonYellow, fontSize = 12)
                        Spacer(modifier = Modifier.height(8.dp))
                        NeonTitle("$gamesWon", fontSize = 20, color = NeonYellow)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    AppTheme {
        ProgressScreenContent(
            coins = 300,
            highScore = 2500,
            gamesPlayed = 50,
            gamesWon = 32,
            onNavigateBack = {}
        )
    }
}
