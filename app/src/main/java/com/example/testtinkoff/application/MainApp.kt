package com.example.testtinkoff.application

import android.app.Application
import com.example.testtinkoff.network.RestApi
import com.example.testtinkoff.utils.Prefs

class MainApp : Application() {
    companion object {
        lateinit var instance: MainApp
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        RestApi.init()
        Prefs.init(this)
    }
}