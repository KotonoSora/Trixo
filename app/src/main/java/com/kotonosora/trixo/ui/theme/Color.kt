package com.kotonosora.trixo.ui.theme

import androidx.compose.ui.graphics.Color

// Neon Arcade Palette
val NeonCyan = Color(0xFF00FFFF)
val NeonMagenta = Color(0xFFFF00FF)
val NeonYellow = Color(0xFFFFFF00)
val NeonGreen = Color(0xFF39FF14)
val NeonRed = Color(0xFFFF3131)
val NeonPurple = Color(0xFFBC13FE)

val AppBackgroundDark = Color(0xFF0A0A12)
val AppSurfaceDark = Color(0xFF151525)

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

val backgroundDark = AppBackgroundDark
val onBackgroundDark = Color.White

val surfaceDark = AppSurfaceDark
val onSurfaceDark = Color.White
val surfaceVariantDark = Color(0xFF202035)
val onSurfaceVariantDark = Color.White

val outlineDark = NeonCyan.copy(alpha = 0.5f)
val outlineVariantDark = Color(0xFF3F484A)
val scrimDark = Color(0xFF000000)
