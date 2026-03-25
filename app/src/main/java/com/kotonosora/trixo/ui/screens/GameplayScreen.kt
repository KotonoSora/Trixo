package com.kotonosora.trixo.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotonosora.trixo.audio.LocalSoundManager
import com.kotonosora.trixo.domain.Difficulty
import com.kotonosora.trixo.domain.GameResult
import com.kotonosora.trixo.domain.GameState
import com.kotonosora.trixo.domain.GameViewModel
import com.kotonosora.trixo.domain.Player
import com.kotonosora.trixo.ui.MainViewModel
import com.kotonosora.trixo.ui.components.NeonButton
import com.kotonosora.trixo.ui.components.NeonIconButton
import com.kotonosora.trixo.ui.components.NeonText
import com.kotonosora.trixo.ui.components.NeonTitle
import com.kotonosora.trixo.ui.theme.NeonCyan
import com.kotonosora.trixo.ui.theme.NeonMagenta
import com.kotonosora.trixo.ui.theme.NeonRed
import com.kotonosora.trixo.ui.theme.NeonYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameplayScreen(
    gameViewModel: GameViewModel,
    mainViewModel: MainViewModel,
    onNavigateToResult: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameState by gameViewModel.gameState.collectAsState()
    val userPreferences by mainViewModel.userPreferences.collectAsState()
    val context = LocalContext.current
    val soundManager = LocalSoundManager.current

    LaunchedEffect(gameState.result) {
        if (gameState.result != GameResult.NONE) {
            val won = gameState.result == GameResult.X_WINS
            val draw = gameState.result == GameResult.DRAW
            
            if (won) {
                soundManager.playWin()
            } else if (gameState.result == GameResult.O_WINS) {
                soundManager.playLose()
            } else if (draw) {
                soundManager.playTap()
            }
            
            mainViewModel.recordGameFinished(won, draw)
            kotlinx.coroutines.delay(1000)
            onNavigateToResult()
        }
    }

    LaunchedEffect(gameState.isAiTurn) {
        if (!gameState.isAiTurn && gameState.board.any { it == Player.O } && gameState.result == GameResult.NONE) {
            soundManager.playTap()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { NeonTitle("GAME", fontSize = 28) },
                navigationIcon = {
                    IconButton(onClick = {
                        // Let's keep manual playTap if we use IconButton without Neon wrapper here
                        soundManager.playTap()
                        onNavigateBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Surrender", tint = NeonCyan, modifier = Modifier.size(32.dp))
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                        Icon(Icons.Rounded.MonetizationOn, contentDescription = "Coins", tint = NeonYellow, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        NeonText(text = "${userPreferences.coins}", color = NeonYellow, fontSize = 18, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            
            PlayerTurnIndicator(gameState = gameState)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                TicTacToeBoard(
                    gameState = gameState,
                    onCellClicked = { index -> 
                        val cell = gameState.board[index]
                        if (cell == Player.NONE && !gameState.isAiTurn && gameState.result == GameResult.NONE) {
                            soundManager.playTap()
                        } else if (cell != Player.NONE && gameState.result == GameResult.NONE) {
                            soundManager.playError()
                        }
                        gameViewModel.playMove(index) 
                    }
                )
            }

            // Power Ups (Only visible in AI mode)
            if (!gameState.isPvP) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PowerUpButton(
                        icon = Icons.Rounded.Lightbulb,
                        cost = 30,
                        onClick = {
                            if (gameState.isAiTurn || gameState.result != GameResult.NONE) return@PowerUpButton
                            if (userPreferences.hints > 0) {
                                mainViewModel.consumeHint(
                                    onSuccess = {
                                        soundManager.playTap()
                                        gameViewModel.requestHint()
                                    },
                                    onFailure = {
                                        soundManager.playError()
                                    }
                                )
                            } else {
                                mainViewModel.spendCoins(30, onSuccess = {
                                    soundManager.playTap()
                                    gameViewModel.requestHint()
                                }, onFailure = {
                                    soundManager.playError()
                                    Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    )
                    PowerUpButton(
                        icon = Icons.Rounded.History,
                        cost = 15,
                        onClick = {
                            if (gameState.isAiTurn || gameState.result != GameResult.NONE || gameState.history.isEmpty()) return@PowerUpButton
                            if (userPreferences.undos > 0) {
                                mainViewModel.consumeUndo(
                                    onSuccess = {
                                        soundManager.playTap()
                                        gameViewModel.undoMove()
                                    },
                                    onFailure = {
                                        soundManager.playError()
                                    }
                                )
                            } else {
                                mainViewModel.spendCoins(15, onSuccess = {
                                    soundManager.playTap()
                                    gameViewModel.undoMove()
                                }, onFailure = {
                                    soundManager.playError()
                                    Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    )
                }
            }

            NeonButton(
                text = "SURRENDER",
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                color = NeonRed,
                height = 64,
                fontSize = 18
            )
        }
    }
}

@Composable
fun PowerUpButton(icon: ImageVector, cost: Int, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f))
                .border(1.dp, NeonCyan.copy(alpha = 0.3f), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        NeonText(text = "$cost", color = NeonYellow, fontSize = 12, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PlayerTurnIndicator(gameState: GameState) {
    val turnText = when {
        gameState.isAiTurn -> "AI THINKING..."
        gameState.isPvP && gameState.currentPlayer == Player.X -> "PLAYER 1 TURN (X)"
        gameState.isPvP && gameState.currentPlayer == Player.O -> "PLAYER 2 TURN (O)"
        else -> "YOUR TURN (X)"
    }
    
    val color = if (gameState.currentPlayer == Player.X) NeonCyan else NeonMagenta

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.1f))
            .border(2.dp, color, RoundedCornerShape(20.dp))
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        NeonText(
            text = turnText,
            color = color,
            fontSize = 16,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TicTacToeBoard(
    gameState: GameState,
    onCellClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            for (row in 0 until gameState.boardSize) {
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until gameState.boardSize) {
                        val index = row * gameState.boardSize + col
                        TicTacToeCell(
                            player = gameState.board[index],
                            isWinningCell = gameState.winningLine?.contains(index) == true,
                            isHintCell = gameState.hintIndex == index,
                            onClick = { onCellClicked(index) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(if (gameState.boardSize > 10) 1.dp else 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TicTacToeCell(
    player: Player,
    isWinningCell: Boolean,
    isHintCell: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isWinningCell -> NeonYellow
        isHintCell -> NeonYellow
        else -> Color.White.copy(alpha = 0.2f)
    }
    
    val backgroundColor = when {
        isWinningCell -> NeonYellow.copy(alpha = 0.25f)
        isHintCell -> NeonYellow.copy(alpha = 0.15f)
        else -> Color.White.copy(alpha = 0.05f)
    }

    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = if (isWinningCell || isHintCell) 2.dp else 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(if (isWinningCell || isHintCell) 4.dp else 2.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(if (isWinningCell || isHintCell) 4.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CellIcon(player)
        }
    }
}

@Composable
fun CellIcon(player: Player) {
    AnimatedVisibility(
        visible = player != Player.NONE,
        enter = scaleIn(animationSpec = tween(300)) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        if (player == Player.X) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "X",
                modifier = Modifier.fillMaxSize(0.8f),
                tint = NeonCyan
            )
        } else if (player == Player.O) {
            Icon(
                imageVector = Icons.Rounded.RadioButtonUnchecked,
                contentDescription = "O",
                modifier = Modifier.fillMaxSize(0.7f),
                tint = NeonMagenta
            )
        }
    }
}
