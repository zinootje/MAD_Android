package com.example.mvp

import android.app.Application
import com.example.data.AppContainer
import com.example.data.DefaultAppContainer

class MvpApplication:Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }
}
