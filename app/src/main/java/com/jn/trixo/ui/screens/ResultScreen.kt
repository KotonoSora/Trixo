package com.jn.trixo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.trixo.audio.LocalSoundManager
import com.jn.trixo.audio.SoundManager
import com.jn.trixo.domain.Difficulty
import com.jn.trixo.domain.GameResult
import com.jn.trixo.domain.GameState
import com.jn.trixo.domain.GameViewModel
import com.jn.trixo.ui.MainViewModel
import com.jn.trixo.ui.components.NeonButton
import com.jn.trixo.ui.components.NeonText
import com.jn.trixo.ui.components.NeonTitle
import com.jn.trixo.ui.components.TrixoTopBar
import com.jn.trixo.ui.theme.NeonGreen
import com.jn.trixo.ui.theme.NeonMagenta
import com.jn.trixo.ui.theme.NeonRed
import com.jn.trixo.ui.theme.NeonYellow
import com.jn.trixo.ui.theme.TrixoTheme

@Composable
fun ResultScreen(
    gameViewModel: GameViewModel,
    mainViewModel: MainViewModel,
    onNavigateHome: () -> Unit,
    onNavigateToGameMode: () -> Unit,
    onNavigateToNextLevel: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    val gameState by gameViewModel.gameState.collectAsState()
    val userPreferences by mainViewModel.userPreferences.collectAsState()

    ResultScreenContent(
        gameState = gameState,
        coins = userPreferences.coins,
        onNavigateHome = onNavigateHome,
        onNavigateToGameMode = onNavigateToGameMode,
        onNavigateToNextLevel = onNavigateToNextLevel,
        modifier = modifier
    )
}

@Composable
fun ResultScreenContent(
    gameState: GameState,
    coins: Int,
    onNavigateHome: () -> Unit,
    onNavigateToGameMode: () -> Unit,
    onNavigateToNextLevel: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    val nextDifficulty = when (gameState.difficulty) {
        Difficulty.EASY -> Difficulty.MEDIUM
        Difficulty.MEDIUM -> Difficulty.HARD
        Difficulty.HARD -> Difficulty.VERY_HARD
        Difficulty.VERY_HARD -> null
    }

    val topBarTitle = when (gameState.result) {
        GameResult.X_WINS -> "VICTORY"
        GameResult.O_WINS -> "GAME OVER"
        GameResult.DRAW -> "DRAW GAME"
        else -> "RESULT"
    }

    Scaffold(
        topBar = {
            TrixoTopBar(
                coins = coins,
                title = topBarTitle
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = true,
                enter = scaleIn(animationSpec = tween(500))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    when (gameState.result) {
                        GameResult.X_WINS -> {
                            NeonTitle("VICTORY!", color = NeonGreen, fontSize = 36)
                            Spacer(modifier = Modifier.height(24.dp))
                            RewardDisplay(amount = 50, iconSize = 32, fontSize = 36)
                        }

                        GameResult.O_WINS -> {
                            NeonTitle("GAME OVER", color = NeonRed, fontSize = 48, textAlign = TextAlign.Center)
                        }

                        GameResult.DRAW -> {
                            NeonTitle("DRAW GAME", color = NeonMagenta, fontSize = 36)
                            Spacer(modifier = Modifier.height(24.dp))
                            RewardDisplay(amount = 10, iconSize = 28, fontSize = 28)
                        }

                        else -> {
                            NeonTitle("ABORTED", color = Color.Gray, fontSize = 36)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (gameState.result == GameResult.X_WINS && nextDifficulty != null && !gameState.isPvP) {
                    NeonButton(
                        text = "NEXT LEVEL",
                        onClick = { onNavigateToNextLevel(nextDifficulty) },
                        modifier = Modifier.fillMaxWidth(),
                        color = NeonGreen
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                NeonButton(
                    text = "PLAY AGAIN",
                    onClick = onNavigateToGameMode,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                NeonButton(
                    text = "HOME",
                    onClick = onNavigateHome,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonMagenta
                )
            }
        }
    }
}

@Composable
private fun RewardDisplay(amount: Int, iconSize: Int, fontSize: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        NeonText(
            "+$amount",
            color = NeonYellow,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Rounded.MonetizationOn,
            contentDescription = "Coins",
            tint = NeonYellow,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

@Preview(showBackground = true, name = "Win State")
@Composable
fun ResultScreenWinPreview() {
    CompositionLocalProvider(LocalSoundManager provides SoundManager(null)) {
        TrixoTheme {
            ResultScreenContent(
                gameState = GameState(result = GameResult.X_WINS, difficulty = Difficulty.EASY),
                coins = 150,
                onNavigateHome = {},
                onNavigateToGameMode = {},
                onNavigateToNextLevel = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Lose State")
@Composable
fun ResultScreenLosePreview() {
    CompositionLocalProvider(LocalSoundManager provides SoundManager(null)) {
        TrixoTheme {
            ResultScreenContent(
                gameState = GameState(result = GameResult.O_WINS, difficulty = Difficulty.MEDIUM),
                coins = 100,
                onNavigateHome = {},
                onNavigateToGameMode = {},
                onNavigateToNextLevel = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Draw State")
@Composable
fun ResultScreenDrawPreview() {
    CompositionLocalProvider(LocalSoundManager provides SoundManager(null)) {
        TrixoTheme {
            ResultScreenContent(
                gameState = GameState(result = GameResult.DRAW, difficulty = Difficulty.HARD),
                coins = 120,
                onNavigateHome = {},
                onNavigateToGameMode = {},
                onNavigateToNextLevel = {}
            )
        }
    }
}
