package com.kotonosora.trixo.ui.screens

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
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Timeline
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToGameMode: () -> Unit,
    onNavigateToCoinShop: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPrefs by viewModel.userPreferences.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Rounded.MonetizationOn,
                            contentDescription = "Coins",
                            tint = NeonYellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        NeonText(
                            text = "${userPrefs.coins}",
                            color = NeonYellow,
                            fontSize = 18,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        NeonIconButton(
                            icon = Icons.Rounded.ShoppingCart,
                            contentDescription = "Shop",
                            onClick = onNavigateToCoinShop,
                            modifier = Modifier.size(48.dp),
                            tint = NeonCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeonTitle("TRIX", fontSize = 48)
                    NeonTitle("O", fontSize = 48, color = NeonMagenta)
                }
            }

            // Menu Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NeonButton(
                    text = "PLAY NOW",
                    onClick = onNavigateToGameMode,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.PlayArrow,
                    height = 72,
                    fontSize = 18
                )

                NeonButton(
                    text = "STATS",
                    onClick = onNavigateToProgress,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonMagenta,
                    icon = Icons.Rounded.Timeline,
                    height = 72,
                    fontSize = 18
                )

                NeonButton(
                    text = "SETTINGS",
                    onClick = onNavigateToSettings,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonYellow,
                    icon = Icons.Rounded.Settings,
                    height = 72,
                    fontSize = 18
                )
            }

            // Bottom spacer for visual balance
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
