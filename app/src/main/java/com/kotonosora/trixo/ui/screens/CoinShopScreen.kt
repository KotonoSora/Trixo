package com.kotonosora.trixo.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotonosora.trixo.audio.LocalSoundManager
import com.kotonosora.trixo.billing.BillingManager
import com.kotonosora.trixo.ui.MainViewModel
import com.kotonosora.trixo.ui.components.NeonButton
import com.kotonosora.trixo.ui.components.NeonIconButton
import com.kotonosora.trixo.ui.components.NeonText
import com.kotonosora.trixo.ui.components.NeonTitle
import com.kotonosora.trixo.ui.theme.NeonCyan
import com.kotonosora.trixo.ui.theme.NeonYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinShopScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val soundManager = LocalSoundManager.current

    val billingManager = remember {
        BillingManager(context, mainViewModel.repository) {
            soundManager.playWin() // Play win sound on successful purchase
        }
    }

    val products by billingManager.products.collectAsState()
    val userPreferences by mainViewModel.userPreferences.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            billingManager.endConnection()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { NeonTitle("COIN SHOP", fontSize = 24) },
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
                        Icon(
                            Icons.Rounded.MonetizationOn,
                            contentDescription = "Coins",
                            tint = NeonYellow
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        NeonText(
                            text = "${userPreferences.coins}",
                            color = NeonYellow,
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            products.forEach { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.MonetizationOn,
                                contentDescription = null,
                                tint = NeonYellow
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                product.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                        NeonButton(
                            text = product.price,
                            onClick = {
                                val activity = context as? Activity
                                if (activity != null) {
                                    billingManager.launchBillingFlow(activity, product)
                                }
                            },
                            height = 48,
                            fontSize = 12
                        )
                    }
                }
            }
        }
    }
}
