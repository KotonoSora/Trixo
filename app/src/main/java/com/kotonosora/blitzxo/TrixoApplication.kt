package com.kotonosora.blitzxo

import android.app.Application
import com.kotonosora.blitzxo.di.AppContainer
import com.kotonosora.blitzxo.di.AppContainerImpl

class TrixoApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
