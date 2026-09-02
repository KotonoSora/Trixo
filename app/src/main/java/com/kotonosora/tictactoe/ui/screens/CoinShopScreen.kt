package com.kotonosora.tictactoe.ui.screens

import android.app.Activity
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.audio.LocalSoundManager
import com.kotonosora.tictactoe.billing.BillingManager
import com.kotonosora.tictactoe.billing.StoreProduct
import com.kotonosora.tictactoe.ui.components.NeonButton
import com.kotonosora.tictactoe.ui.components.NeonText
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.viewmodels.MainViewModel

@Composable
fun CoinShopScreen(
    mainViewModel: MainViewModel,
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
    val error by billingManager.error.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            billingManager.endConnection()
        }
    }

    CoinShopContent(
        products = products,
        error = error,
        onTryAgain = { billingManager.startConnection() },
        onPurchaseProduct = { product ->
            val activity = context as? Activity
            if (activity != null) {
                billingManager.launchBillingFlow(activity, product)
            }
        },
        modifier = modifier
    )
}

@Composable
fun CoinShopContent(
    products: List<StoreProduct>,
    error: String?,
    onTryAgain: () -> Unit,
    onPurchaseProduct: (StoreProduct) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NeonText(
                    error,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 14
                )
                Spacer(modifier = Modifier.height(24.dp))
                NeonButton(
                    text = "TRY AGAIN",
                    onClick = onTryAgain,
                    height = 48,
                    fontSize = 12
                )
            }
        } else if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                NeonText(
                    "No items currently for sale",
                    color = Color.Gray,
                    fontSize = 12,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            products.forEach { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = 0.3f
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.MonetizationOn,
                                    contentDescription = null,
                                    tint = NeonYellow,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                NeonText(
                                    product.title,
                                    fontSize = 14,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            NeonButton(
                                text = product.price,
                                onClick = { onPurchaseProduct(product) },
                                height = 40,
                                fontSize = 10
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinShopScreenPreview() {
    AppTheme {
        CoinShopContent(
            products = listOf(
                StoreProduct("coins_100", "100 Coins", "$0.29", "A pack of 100 coins."),
                StoreProduct("coins_500", "500 Coins", "$0.49", "A pack of 500 coins.")
            ),
            error = null,
            onTryAgain = {},
            onPurchaseProduct = {}
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun CoinShopErrorPreview() {
    AppTheme {
        CoinShopContent(
            products = emptyList(),
            error = "Store currently unavailable",
            onTryAgain = {},
            onPurchaseProduct = {}
        )
    }
}
