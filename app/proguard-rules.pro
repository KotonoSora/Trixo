# Jetpack Compose
-keepclassmembers class androidx.compose.ui.platform.AndroidComposeView {
    void *;
}
-keep class androidx.compose.runtime.Recomposer { *; }

# ViewModel
-keep class * extends androidx.lifecycle.ViewModel

# Coil
-dontwarn coil.**

# Google Play Billing
-dontwarn com.android.billingclient.api.**

# Game Models (Keep for persistence/serialization)
-keep class com.kotonosora.tictactoe.domain.model.** { *; }
-keepattributes *Annotation*, InnerClasses, EnclosingMethod, Signature, SourceFile, LineNumberTable

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-dontwarn kotlinx.coroutines.**
