package com.kotonosora.trixo.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.kotonosora.trixo.ui.MainViewModel
import com.kotonosora.trixo.ui.components.NeonButton
import com.kotonosora.trixo.ui.components.NeonIconButton
import com.kotonosora.trixo.ui.components.NeonText
import com.kotonosora.trixo.ui.components.NeonTitle
import com.kotonosora.trixo.ui.theme.NeonCyan
import com.kotonosora.trixo.ui.theme.NeonMagenta
import com.kotonosora.trixo.ui.theme.NeonYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PowerUpScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCoinShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPreferences by mainViewModel.userPreferences.collectAsState()
    val context = LocalContext.current
    val soundManager = LocalSoundManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { NeonTitle("POWER-UPS", fontSize = 24) },
                navigationIcon = {
                    NeonIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        onClick = onNavigateBack,
                        tint = NeonCyan
                    )
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.MonetizationOn, contentDescription = "Coins", tint = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.width(8.dp))
                        NeonText(
                            text = "${userPreferences.coins}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            PowerUpItem(
                name = "AI HINT",
                description = "Shows the best move.",
                icon = Icons.Rounded.Lightbulb,
                count = userPreferences.hints,
                cost = 50,
                color = NeonMagenta,
                onBuy = {
                    mainViewModel.spendCoins(
                        amount = 50,
                        onSuccess = {
                            mainViewModel.addHints(1)
                            soundManager.playWin()
                            Toast.makeText(context, "HINT PURCHASED!", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            soundManager.playError()
                            Toast.makeText(context, "NOT ENOUGH COINS!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PowerUpItem(
                name = "UNDO",
                description = "Revert your last move.",
                icon = Icons.Rounded.History,
                count = userPreferences.undos,
                cost = 30,
                color = NeonYellow,
                onBuy = {
                    mainViewModel.spendCoins(
                        amount = 30,
                        onSuccess = {
                            mainViewModel.addUndos(1)
                            soundManager.playWin()
                            Toast.makeText(context, "UNDO PURCHASED!", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            soundManager.playError()
                            Toast.makeText(context, "NOT ENOUGH COINS!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(48.dp))

            NeonButton(
                text = "GET MORE COINS",
                onClick = onNavigateToCoinShop,
                modifier = Modifier.fillMaxWidth(),
                color = NeonCyan,
                icon = Icons.Rounded.ShoppingCart
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PowerUpItem(
    name: String,
    description: String,
    icon: ImageVector,
    count: Int,
    cost: Int,
    color: Color,
    onBuy: () -> Unit
) {
    // This is passed to NeonButton below, so we don't need to manually add playTap
    // NeonButton will call playTap internally. But wait!
    // If NeonButton calls playTap, we'll hear a tap sound, followed by either playWin or playError 
    // depending on success. That might be a bit noisy but generally OK.
    // If we want to avoid double sounds, we could modify NeonButton, but it's okay for now.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(color.copy(alpha = 0.05f))
            .border(2.dp, color, RoundedCornerShape(24.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                modifier = Modifier.size(64.dp),
                tint = NeonYellow
            )
            Spacer(modifier = Modifier.height(16.dp))
            NeonTitle(name, color = color, fontSize = 24)
            Spacer(modifier = Modifier.height(16.dp))
            NeonText(
                text = description,
                color = Color.White,
                fontSize = 12
            )
            Spacer(modifier = Modifier.height(16.dp))
            NeonText(
                text = "OWNED: $count",
                color = NeonCyan,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            NeonButton(
                text = "BUY ($cost C)",
                onClick = onBuy,
                modifier = Modifier.fillMaxWidth(),
                color = color
            )
        }
    }
}
