package com.kotonosora.trixo.ui.theme

import androidx.compose.ui.graphics.Color

// Neon Arcade Palette (SudoBlitz Reference)
val DarkBackground = Color(0xFF0A0A12)
val NeonGreen = Color(0xFF39FF14)
val NeonYellow = Color(0xFFFFEA00)
val NeonCyan = Color(0xFF00FFFF)
val NeonMagenta = Color(0xFFFF00FF)
val NeonRed = Color(0xFFFF003C)
val NeonBlue = Color(0xFF0066FF)
val GridLineColor = Color(0xFF222244)
val SurfaceDarkColor = Color(0xFF1A1A2E)

// Semantic Tokens
val ErrorRed = NeonRed
val SuccessGreen = NeonGreen
val CoinGold = NeonYellow
val HighlightYellow = NeonYellow

// Material 3 Dark Color Scheme (Strictly Dark)
val primaryDark = NeonCyan
val onPrimaryDark = Color.Black
val primaryContainerDark = NeonCyan.copy(alpha = 0.15f)
val onPrimaryContainerDark = NeonCyan

val secondaryDark = NeonMagenta
val onSecondaryDark = Color.Black
val secondaryContainerDark = NeonMagenta.copy(alpha = 0.15f)
val onSecondaryContainerDark = NeonMagenta

val tertiaryDark = NeonYellow
val onTertiaryDark = Color.Black
val tertiaryContainerDark = NeonYellow.copy(alpha = 0.15f)
val onTertiaryContainerDark = NeonYellow

val errorDark = NeonRed
val onErrorDark = Color.Black
val errorContainerDark = NeonRed.copy(alpha = 0.15f)
val onErrorContainerDark = NeonRed

val backgroundDark = DarkBackground
val onBackgroundDark = Color.White

val surfaceDark = SurfaceDarkColor
val onSurfaceDark = Color.White
val surfaceVariantDark = Color(0xFF202035)
val onSurfaceVariantDark = Color.White

val outlineDark = NeonCyan.copy(alpha = 0.5f)
val outlineVariantDark = Color(0xFF3F484A)
val scrimDark = Color(0xFF000000)
