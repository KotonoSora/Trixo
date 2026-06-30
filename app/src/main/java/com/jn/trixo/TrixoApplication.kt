package com.jn.trixo

import android.app.Application
import com.jn.trixo.di.AppContainer
import com.jn.trixo.di.AppContainerImpl

class TrixoApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
