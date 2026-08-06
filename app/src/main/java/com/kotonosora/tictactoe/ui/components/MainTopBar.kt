package com.kotonosora.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonYellow

@Composable
fun MainTopBar(
    coins: Int,
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    onShopClick: (() -> Unit)? = null,
    showShopIcon: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp)
    ) {
        // Left Section: Back Button
        if (onBackClick != null) {
            NeonIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                onClick = onBackClick,
                tint = NeonCyan,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        // Center Section: Title
        if (title != null) {
            NeonTitle(
                text = title,
                fontSize = 16,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 60.dp, end = 80.dp), // Avoid overlap with buttons
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Right Section: Coins and Shop
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoinCapsule(coins = coins, onClick = onShopClick)

            if (showShopIcon && onShopClick != null) {
                NeonIconButton(
                    icon = Icons.Rounded.ShoppingCart,
                    contentDescription = "Shop",
                    onClick = onShopClick,
                    modifier = Modifier.size(40.dp),
                    tint = NeonCyan
                )
            }
        }
    }
}

@Composable
fun CoinCapsule(
    coins: Int,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(NeonYellow.copy(alpha = 0.1f))
            .border(1.dp, NeonYellow.copy(alpha = 0.5f), RoundedCornerShape(50))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Rounded.MonetizationOn,
                contentDescription = null,
                tint = NeonYellow,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            NeonText(
                text = coins.toString(),
                color = NeonYellow,
                fontSize = 12,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun MainTopBarPreview() {
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            MainTopBar(coins = 500, showShopIcon = true)
            MainTopBar(coins = 1200, title = "SCREEN TITLE", onBackClick = {})
            MainTopBar(coins = 2500, title = "SHOP", onBackClick = {}, onShopClick = {}, showShopIcon = true)
        }
    }
}
