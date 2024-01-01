package com.example.mvp

import android.app.Application
import com.example.data.AppContainer
import com.example.data.DefaultAppContainer

/**
 * MvpApplication is a subclass of Application that initializes the AppContainer and sets it as the container for the application.
 */
class MvpApplication:Application() {
    lateinit var container: AppContainer

    /**
     * The `onCreate` method is a lifecycle callback method of the `MvpApplication` class that overrides the `onCreate` method of the `Application` class.
     * It is responsible for initializing the `AppContainer` and setting it as the container for the application.
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }
}
