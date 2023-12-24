package com.example.mvp

import android.app.Application
import com.example.mvp.data.AppContainer
import com.example.mvp.data.DefaultAppContainer

class MvpApplication:Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }
}
