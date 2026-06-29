package com.jn.trixo.billing

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.jn.trixo.data.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class StoreProduct(
    val productId: String,
    val title: String,
    val price: String,
    val originalDetails: ProductDetails? = null
)

class BillingManager(
    private val context: Context,
    private val preferencesRepository: UserPreferencesRepository,
    private val onPurchaseSuccess: (Int) -> Unit = {}
) : PurchasesUpdatedListener {

    private val isDebug = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    private val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
        .enableOneTimeProducts()
        .build()

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(pendingPurchasesParams)
        .build()

    private val _products = MutableStateFlow<List<StoreProduct>>(emptyList())
    val products: StateFlow<List<StoreProduct>> = _products.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        val COIN_PACKS = mapOf(
            "coins_100" to 100,
            "coins_500" to 500,
            "coins_1000" to 1000,
            "coins_1500" to 1500,
            "coins_2000" to 2000,
            "coins_2500" to 2500,
            "coins_3000" to 3000,
            "coins_3500" to 3500,
            "coins_4000" to 4000
        )
    }

    init {
        if (isDebug) {
            queryProducts()
        } else {
            startConnection()
        }
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Connection lost. It's better to wait for the next interaction to reconnect
                // or use an exponential backoff strategy if it's critical.
            }
        })
    }

    private fun queryProducts() {
        if (isDebug) {
            val mockProducts = COIN_PACKS.map { (id, coins) ->
                StoreProduct(
                    productId = id,
                    title = "$coins Coins",
                    price = when (id) {
                        "coins_100" -> "$0.29"
                        "coins_500" -> "$0.49"
                        "coins_1000" -> "$0.69"
                        "coins_1500" -> "$0.99"
                        "coins_2000" -> "$1.99"
                        "coins_2500" -> "$3.99"
                        "coins_3000" -> "$4.99"
                        "coins_3500" -> "$7.99"
                        "coins_4000" -> "$9.99"
                        else -> "Unknown"
                    }
                )
            }.sortedBy { COIN_PACKS[it.productId] ?: 0 }
            _products.value = mockProducts
            return
        }

        val productIds = COIN_PACKS.keys.toList()
        val productList = productIds.map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, result ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val sortedProducts = result.productDetailsList.sortedBy { product ->
                    COIN_PACKS[product.productId] ?: Int.MAX_VALUE
                }.map { product ->
                    StoreProduct(
                        productId = product.productId,
                        title = product.name,
                        price = product.oneTimePurchaseOfferDetails?.formattedPrice ?: "Unknown",
                        originalDetails = product
                    )
                }
                _products.value = sortedProducts
            }
        }
    }

    fun launchBillingFlow(activity: Activity, product: StoreProduct) {
        if (isDebug && product.originalDetails == null) {
            // Mock purchase flow
            grantCoins(listOf(product.productId))
            return
        }

        val originalDetails = product.originalDetails ?: return

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(originalDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // All products are consumables (coins)
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    grantCoins(purchase.products)
                }
            }
        }
    }

    private fun grantCoins(productIds: List<String>) {
        scope.launch {
            var coinsToAdd = 0
            for (productId in productIds) {
                coinsToAdd += COIN_PACKS[productId] ?: 0
            }
            if (coinsToAdd > 0) {
                preferencesRepository.addCoins(coinsToAdd)
                withContext(Dispatchers.Main) {
                    onPurchaseSuccess(coinsToAdd)
                }
            }
        }
    }

    fun endConnection() {
        billingClient.endConnection()
        scope.cancel()
    }
}
