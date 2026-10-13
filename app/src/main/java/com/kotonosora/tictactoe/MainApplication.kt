package com.kotonosora.tictactoe

import android.app.Application
import local.trixo.ui.di.AppContainer
import local.trixo.ui.di.AppContainerHost
import local.trixo.ui.di.DefaultAppContainer

class MainApplication : Application(), AppContainerHost {
    override lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, BuildConfig.DEBUG)
    }
}
