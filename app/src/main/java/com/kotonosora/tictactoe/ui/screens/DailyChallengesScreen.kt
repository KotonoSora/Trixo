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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonGreen
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.theme.PressStart2PFontFamily
import com.kotonosora.tictactoe.ui.viewmodels.DailyChallenge
import com.kotonosora.tictactoe.ui.viewmodels.DailyChallengesEvent
import com.kotonosora.tictactoe.ui.viewmodels.DailyChallengesViewModel
import com.kotonosora.tictactoe.ui.viewmodels.MainEvent
import com.kotonosora.tictactoe.ui.viewmodels.MainViewModel

@Composable
fun DailyChallengesScreen(
    mainViewModel: MainViewModel,
    dailyChallengesViewModel: DailyChallengesViewModel,
    onNavigateToPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by dailyChallengesViewModel.uiState.collectAsState()

    DailyChallengesContent(
        challenges = uiState.challenges,
        onClaimReward = { challengeId ->
            dailyChallengesViewModel.onEvent(
                DailyChallengesEvent.ClaimReward(challengeId) { coins ->
                    mainViewModel.onEvent(MainEvent.AddCoins(coins))
                }
            )
        },
        onPlay = onNavigateToPlay,
        modifier = modifier
    )
}

@Composable
fun DailyChallengesContent(
    challenges: List<DailyChallenge>,
    onClaimReward: (String) -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(challenges, key = { it.id }) { challenge ->
            ChallengeItem(
                challenge = challenge,
                onClaimReward = { onClaimReward(challenge.id) },
                onPlay = onPlay
            )
        }
    }
}

@Composable
fun ChallengeItem(
    challenge: DailyChallenge,
    onClaimReward: () -> Unit,
    onPlay: () -> Unit
) {
    val progress = challenge.progress.toFloat() / challenge.total

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(challenge.color.copy(alpha = 0.05f))
            .border(2.dp, challenge.color, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    NeonText(challenge.title, color = challenge.color, fontSize = 18)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.MonetizationOn,
                            contentDescription = null,
                            tint = NeonYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${challenge.rewardCoins}",
                            color = NeonYellow,
                            fontFamily = PressStart2PFontFamily,
                            fontSize = 10.sp
                        )
                    }
                }

                if (challenge.isClaimed) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NeonText("CLAIMED", color = NeonGreen, fontSize = 10)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else if (challenge.isCompleted) {
                    TextButton(
                        onClick = onClaimReward,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = challenge.color.copy(alpha = 0.2f),
                            contentColor = challenge.color
                        ),
                        modifier = Modifier
                            .border(1.dp, challenge.color, RoundedCornerShape(8.dp))
                            .height(36.dp)
                    ) {
                        Text(
                            "CLAIM",
                            fontFamily = PressStart2PFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    TextButton(
                        onClick = onPlay,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = challenge.color.copy(alpha = 0.1f),
                            contentColor = challenge.color
                        ),
                        modifier = Modifier
                            .border(
                                1.dp,
                                challenge.color.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            )
                            .height(36.dp)
                    ) {
                        Text(
                            "PLAY",
                            fontFamily = PressStart2PFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            NeonText(challenge.description, color = Color.White.copy(alpha = 0.7f), fontSize = 12)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = challenge.color,
                trackColor = challenge.color.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                NeonText(
                    "${challenge.progress}/${challenge.total}",
                    color = challenge.color,
                    fontSize = 12
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyChallengesPreview() {
    AppTheme {
        DailyChallengesContent(
            challenges = listOf(
                DailyChallenge("1", "GAMER", "Play 5 games", 2, 5, 50, NeonCyan),
                DailyChallenge("2", "WINNER", "Win 2 games", 2, 2, 100, NeonMagenta),
                DailyChallenge(
                    "3",
                    "STRATEGIST",
                    "Use 3 hints",
                    0,
                    3,
                    30,
                    NeonGreen,
                    isClaimed = true
                )
            ),
            onClaimReward = {},
            onPlay = {}
        )
    }
}
