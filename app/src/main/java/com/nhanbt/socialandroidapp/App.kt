package com.nhanbt.socialandroidapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        private lateinit var app: App

        fun getAppContext(): Context {
            return app.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}
