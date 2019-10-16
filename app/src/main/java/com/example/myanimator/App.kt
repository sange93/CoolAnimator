package com.example.myanimator

import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {

    companion object {
        // app实例
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}