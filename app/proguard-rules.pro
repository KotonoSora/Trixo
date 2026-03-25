# -----------------------------------------------------------------------------------
# General Android Rules
# -----------------------------------------------------------------------------------

# Preserve line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# Preserve Annotations and Signatures for Reflection-heavy libraries
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# -----------------------------------------------------------------------------------
# Jetpack Compose
# -----------------------------------------------------------------------------------
-keepclassmembers class androidx.compose.ui.platform.ComposeView {
   public *;
}
-keep class androidx.compose.runtime.Recomposer { *; }

# -----------------------------------------------------------------------------------
# Room Database
# -----------------------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# -----------------------------------------------------------------------------------
# Retrofit & OkHttp
# -----------------------------------------------------------------------------------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes RuntimeVisibleAlphaAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# -----------------------------------------------------------------------------------
# Moshi (JSON Library)
# -----------------------------------------------------------------------------------
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonQualifier interface *

# Keep generated Moshi adapters
-keep class *JsonAdapter { *; }
-keep class *JsonAdapter {
    <init>(...);
}
-keep @com.squareup.moshi.JsonClass class *

# -----------------------------------------------------------------------------------
# Kotlin Coroutines
# -----------------------------------------------------------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-dontwarn kotlinx.coroutines.**

# -----------------------------------------------------------------------------------
# Google Play Billing
# -----------------------------------------------------------------------------------
-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**

# -----------------------------------------------------------------------------------
# Coil (Image Loading)
# -----------------------------------------------------------------------------------
-dontwarn coil.**

# -----------------------------------------------------------------------------------
# App Specific Models
# -----------------------------------------------------------------------------------
# Keep all data models and domain objects to prevent issues with serialization/DB
-keep class com.kotonosora.trixo.domain.** { *; }
-keep class com.kotonosora.trixo.data.** { *; }
-keepclassmembers class com.kotonosora.trixo.domain.** { *; }
-keepclassmembers class com.kotonosora.trixo.data.** { *; }
