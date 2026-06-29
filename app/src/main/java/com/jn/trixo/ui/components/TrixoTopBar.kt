package com.jn.trixo.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jn.trixo.ui.theme.NeonCyan
import com.jn.trixo.ui.theme.NeonYellow

@Composable
fun TrixoTopBar(
    coins: Int,
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    onShopClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            NeonIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                onClick = onBackClick,
                tint = NeonCyan
            )
        }

        if (title != null) {
            if (onBackClick == null) {
                Spacer(modifier = Modifier.width(4.dp))
            }
            NeonTitle(title, fontSize = 24, textAlign = TextAlign.Start)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                Icons.Rounded.MonetizationOn,
                contentDescription = "Coins",
                tint = NeonYellow,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            NeonText(
                text = "$coins",
                color = NeonYellow,
                fontSize = 18,
                fontWeight = FontWeight.Bold
            )

            if (onShopClick != null) {
                Spacer(modifier = Modifier.width(12.dp))
                NeonIconButton(
                    icon = Icons.Rounded.ShoppingCart,
                    contentDescription = "Shop",
                    onClick = onShopClick,
                    modifier = Modifier.size(48.dp),
                    tint = NeonCyan
                )
            }
        }
    }
}
