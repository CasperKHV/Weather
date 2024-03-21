package com.casperkhv.weather

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log

internal class ActivityLifecycleLoggingCallback : ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(TAG, "${activity.localClassName} : Created")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "${activity.localClassName} : Started")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "${activity.localClassName} : Resumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "${activity.localClassName} : Paused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "${activity.localClassName} : Stopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(TAG, "${activity.localClassName} : SaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "${activity.localClassName} : Destroyed")
    }

    companion object {
        private const val TAG = "lifecycle/activity"
    }
}