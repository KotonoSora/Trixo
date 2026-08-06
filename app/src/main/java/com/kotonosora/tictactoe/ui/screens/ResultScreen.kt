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
import com.kotonosora.tictactoe.audio.LocalSoundManager
import com.kotonosora.tictactoe.audio.SoundManager
import com.kotonosora.tictactoe.domain.Difficulty
import com.kotonosora.tictactoe.domain.GameResult
import com.kotonosora.tictactoe.domain.GameState
import com.kotonosora.tictactoe.domain.GameViewModel
import com.kotonosora.tictactoe.ui.MainViewModel
import com.kotonosora.tictactoe.ui.components.NeonButton
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.components.NeonTitle
import com.kotonosora.tictactoe.ui.components.MainTopBar
import com.kotonosora.tictactoe.ui.theme.NeonGreen
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonRed
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.theme.AppTheme

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
            MainTopBar(
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
                .navigationBarsPadding()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                when (gameState.result) {
                    GameResult.X_WINS -> {
                        // Fixed NoSuchMethodError by using explicit named arguments and refreshing the call site signature
                        NeonTitle(
                            text = "VICTORY!",
                            color = NeonGreen,
                            fontSize = 40,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NeonText(
                            text = "Score: ${gameState.score}",
                            color = Color.White,
                            fontSize = 20
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        RewardDisplay(amount = gameState.reward, iconSize = 32, fontSize = 36)
                    }

                    GameResult.O_WINS -> {
                        NeonTitle(
                            text = "GAME OVER",
                            color = NeonRed,
                            fontSize = 48,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NeonText(
                            text = "Score: ${gameState.score}",
                            color = Color.White,
                            fontSize = 20
                        )
                    }

                    GameResult.DRAW -> {
                        NeonTitle(
                            text = "DRAW GAME",
                            color = NeonMagenta,
                            fontSize = 48,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NeonText(
                            text = "Score: ${gameState.score}",
                            color = Color.White,
                            fontSize = 20
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        RewardDisplay(amount = gameState.reward, iconSize = 28, fontSize = 28)
                    }

                    else -> {
                        NeonTitle(text = "ABORTED", color = Color.Gray, fontSize = 36)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

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
        AppTheme {
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
        AppTheme {
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
        AppTheme {
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
