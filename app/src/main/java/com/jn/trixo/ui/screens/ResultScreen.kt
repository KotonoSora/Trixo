package com.jn.trixo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material.icons.rounded.SentimentNeutral
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jn.trixo.domain.GameResult
import com.jn.trixo.domain.GameViewModel
import com.jn.trixo.ui.MainViewModel
import com.jn.trixo.ui.components.NeonButton
import com.jn.trixo.ui.components.NeonText
import com.jn.trixo.ui.components.NeonTitle
import com.jn.trixo.ui.components.TrixoTopBar
import com.jn.trixo.ui.theme.NeonCyan
import com.jn.trixo.ui.theme.NeonMagenta
import com.jn.trixo.ui.theme.NeonRed
import com.jn.trixo.ui.theme.NeonYellow

@Composable
fun ResultScreen(
    gameViewModel: GameViewModel,
    mainViewModel: MainViewModel,
    onNavigateHome: () -> Unit,
    onNavigateToGameMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameState by gameViewModel.gameState.collectAsState()
    val userPreferences by mainViewModel.userPreferences.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold(
        topBar = {
            TrixoTopBar(
                coins = userPreferences.coins,
                title = "GAME OVER"
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = scaleIn(animationSpec = tween(500))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    when (gameState.result) {
                        GameResult.X_WINS -> {
                            Icon(
                                imageVector = Icons.Rounded.EmojiEvents,
                                contentDescription = "Win",
                                modifier = Modifier.size(120.dp),
                                tint = NeonCyan
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            NeonTitle("YOU WIN!", color = NeonCyan)
                            Spacer(modifier = Modifier.height(8.dp))
                            NeonText("+50 COINS", color = NeonYellow, fontWeight = FontWeight.Bold)
                        }

                        GameResult.O_WINS -> {
                            Icon(
                                imageVector = Icons.Rounded.SentimentDissatisfied,
                                contentDescription = "Lose",
                                modifier = Modifier.size(120.dp),
                                tint = NeonRed
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            NeonTitle("AI WINS!", color = NeonRed)
                        }

                        GameResult.DRAW -> {
                            Icon(
                                imageVector = Icons.Rounded.SentimentNeutral,
                                contentDescription = "Draw",
                                modifier = Modifier.size(120.dp),
                                tint = NeonMagenta
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            NeonTitle("DRAW!", color = NeonMagenta)
                            Spacer(modifier = Modifier.height(8.dp))
                            NeonText("+10 COINS", color = NeonYellow, fontWeight = FontWeight.Bold)
                        }

                        else -> {
                            NeonTitle("INTERRUPTED", color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

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
