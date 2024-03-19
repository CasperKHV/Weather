package com.casperkhv.weather

import android.app.Application
import android.util.Log

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("WeatherApplication", "onCreate")
        val activityLifecycleLog: ActivityLifecycleLog = ActivityLifecycleLog()
        registerActivityLifecycleCallbacks(activityLifecycleLog)
    }

}