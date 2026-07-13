package com.kiro.poc.presentation

import android.app.Application
import com.kiro.poc.di.AppComponent
import com.kiro.poc.di.DaggerAppComponent

class MainApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}
