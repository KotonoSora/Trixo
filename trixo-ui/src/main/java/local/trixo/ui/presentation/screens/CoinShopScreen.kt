package local.trixo.ui.presentation.screens

import android.app.Activity
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import local.core.billing.BillingStatus
import local.core.shop.coins.CoinPackItem
import local.core.shop.coins.CoinPacks
import local.core.shop.coins.mapToCoinPack
import local.trixo.ui.infrastructure.audio.LocalSoundManager
import local.trixo.ui.presentation.components.NeonButton
import local.trixo.ui.presentation.components.NeonText
import local.trixo.ui.di.LocalAppContainer
import local.trixo.ui.presentation.theme.AppTheme
import local.trixo.ui.presentation.theme.NeonYellow
import local.trixo.ui.presentation.viewmodel.MainViewModel

@Composable
fun CoinShopScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val soundManager = LocalSoundManager.current
    val container = LocalAppContainer.current
    val billingRepository = remember(container) { container.billingRepository }

    val status by billingRepository.status.collectAsState(initial = BillingStatus.IDLE)
    val products by billingRepository.products.collectAsState(initial = emptyList())
    val packs = remember(products) { products.map { mapToCoinPack(it) } }

    val uiState by mainViewModel.uiState.collectAsState()
    var prevCoins by remember { mutableIntStateOf(uiState.userPreferences.coins) }
    LaunchedEffect(uiState.userPreferences.coins) {
        if (uiState.userPreferences.coins > prevCoins) {
            soundManager.playWin()
        }
        prevCoins = uiState.userPreferences.coins
    }

    LaunchedEffect(billingRepository) {
        billingRepository.startConnection()
    }

    CoinShopContent(
        products = packs,
        error = if (status == BillingStatus.ERROR) "Store currently unavailable" else null,
        isEmpty = status == BillingStatus.EMPTY || (status == BillingStatus.CONNECTED && packs.isEmpty()),
        onTryAgain = { billingRepository.startConnection() },
        onPurchaseProduct = { item ->
            val activity = context as? Activity ?: return@CoinShopContent
            billingRepository.launchPurchaseFlow(activity, item.id)
        },
        modifier = modifier
    )
}

@Composable
fun CoinShopContent(
    products: List<CoinPackItem>,
    error: String?,
    isEmpty: Boolean,
    onTryAgain: () -> Unit,
    onPurchaseProduct: (CoinPackItem) -> Unit,
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
        } else if (isEmpty) {
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
                                Image(
                                    painter = painterResource(id = product.imageRes),
                                    contentDescription = product.name,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    NeonText(
                                        product.name,
                                        fontSize = 14,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    NeonText(
                                        product.description,
                                        fontSize = 9,
                                        color = Color.Gray
                                    )
                                }
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
            products = CoinPacks.mockProducts.map { mapToCoinPack(it) },
            error = null,
            isEmpty = false,
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
            isEmpty = false,
            onTryAgain = {},
            onPurchaseProduct = {}
        )
    }
}
