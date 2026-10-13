# Trixo UI (screens, VMs, adapters)
-keep class local.trixo.ui.** { *; }
-keep class local.trixo.engine.** { *; }
# Room (via core-ui) — keep here too in case UI references entities
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
