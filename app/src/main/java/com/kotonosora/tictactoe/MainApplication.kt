package com.kotonosora.tictactoe

import android.app.Application
import com.kotonosora.tictactoe.di.AppContainer
import com.kotonosora.tictactoe.di.AppContainerImpl

class MainApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
