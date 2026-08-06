package com.kotonosora.tictactoe.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.theme.AppTheme

@Composable
fun HelpScreen(
    modifier: Modifier = Modifier
) {
    HelpScreenContent(
        modifier = modifier
    )
}

@Composable
fun HelpScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        NeonText(
            text = "HOW TO PLAY",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20
        )
        Spacer(modifier = Modifier.height(16.dp))
        NeonText(
            text = "BlitzXO is a fast-paced Tic-Tac-Toe game with neon effects and daily challenges.",
            fontSize = 14
        )
        Spacer(modifier = Modifier.height(24.dp))
        NeonText(
            text = "GOAL",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20
        )
        Spacer(modifier = Modifier.height(16.dp))
        NeonText(
            text = "Get three of your marks in a horizontal, vertical, or diagonal row to win.",
            fontSize = 14
        )
        Spacer(modifier = Modifier.height(24.dp))
        NeonText(
            text = "COINS & SHOP",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20
        )
        Spacer(modifier = Modifier.height(16.dp))
        NeonText(
            text = "Earn coins by winning games and completing daily challenges. Use coins to buy power-ups and skins in the shop.",
            fontSize = 14
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    AppTheme {
        HelpScreenContent()
    }
}
