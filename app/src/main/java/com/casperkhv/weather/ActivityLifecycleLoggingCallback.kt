package com.casperkhv.weather

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity

internal class ActivityLifecycleLoggingCallback : ActivityLifecycleCallbacks {

    private val fragmentLifecycleLoggingCallback = FragmentLifecycleLoggingCallback()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(TAG, "${activity.localClassName} : Created")
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleLoggingCallback, true)
        }
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
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleLoggingCallback)
        }
    }

    companion object {
        private const val TAG = "lifecycle/activity"
    }
}