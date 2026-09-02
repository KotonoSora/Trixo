package com.kotonosora.tictactoe.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotonosora.tictactoe.audio.LocalSoundManager
import com.kotonosora.tictactoe.ui.theme.AppTheme
import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.theme.NeonYellow
import com.kotonosora.tictactoe.ui.theme.PressStart2PFontFamily


@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = NeonCyan,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: Int = 64,
    fontSize: Int = 16
) {
    val soundManager = LocalSoundManager.current

    Button(
        onClick = {
            soundManager.playTap()
            onClick()
        },
        modifier = modifier
            .height(height.dp)
            .border(2.dp, color, RoundedCornerShape(50)),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.15f),
            contentColor = color,
            disabledContainerColor = color.copy(alpha = 0.05f),
            disabledContentColor = color.copy(alpha = 0.3f)
        ),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = fontSize.sp,
                    fontFamily = PressStart2PFontFamily
                ),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun NeonIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = NeonCyan
) {
    val soundManager = LocalSoundManager.current
    IconButton(
        onClick = {
            soundManager.playTap()
            onClick()
        },
        modifier = modifier
    ) {
        Icon(icon, contentDescription = contentDescription, tint = tint)
    }
}

@Composable
fun NeonTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonCyan,
    fontSize: Int = 36,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        style = TextStyle(
            fontFamily = PressStart2PFontFamily,
            color = color,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            shadow = Shadow(
                color = color,
                blurRadius = 16f
            )
        )
    )
}

@Composable
fun NeonText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        fontFamily = PressStart2PFontFamily,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun NeonComponentsPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NeonTitle("TITLE")
            NeonTitle("MAGENTA", color = NeonMagenta, fontSize = 24)

            NeonText("Regular text")
            NeonText("Yellow text", color = NeonYellow, fontWeight = FontWeight.Bold)

            NeonButton(text = "BUTTON", onClick = {})
            NeonButton(
                text = "WITH ICON",
                onClick = {},
                icon = Icons.Rounded.PlayArrow,
                color = NeonYellow
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                NeonIconButton(icon = Icons.Rounded.PlayArrow, onClick = {})
                NeonIconButton(icon = Icons.Rounded.PlayArrow, onClick = {}, tint = NeonMagenta)
            }
        }
    }
}
