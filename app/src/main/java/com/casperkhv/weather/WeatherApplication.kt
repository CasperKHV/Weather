package com.casperkhv.weather

import android.app.Application
import android.util.Log

internal class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("WeatherApplication", "onCreate")
        val activityLifecycleLoggingCallback = ActivityLifecycleLoggingCallback()
        registerActivityLifecycleCallbacks(activityLifecycleLoggingCallback)
    }
}