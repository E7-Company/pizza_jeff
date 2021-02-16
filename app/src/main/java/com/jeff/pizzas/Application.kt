package com.jeff.pizzas

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}