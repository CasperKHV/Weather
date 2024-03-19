package com.casperkhv.weather

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log

class ActivityLifecycleLog : ActivityLifecycleCallbacks {

    private val TAG: String = "ActivityLifecycleLog"

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        Log.d(TAG, "${p0.localClassName} now created")
    }

    override fun onActivityStarted(p0: Activity) {
        Log.d(TAG, "${p0.localClassName} now started")
    }

    override fun onActivityResumed(p0: Activity) {
        Log.d(TAG, "${p0.localClassName} now resumed")
    }

    override fun onActivityPaused(p0: Activity) {
        Log.d(TAG, "${p0.localClassName} now paused")
    }

    override fun onActivityStopped(p0: Activity) {
        Log.d(TAG, "${p0.localClassName} now stopped")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        Log.d(TAG, "${p0.localClassName} now saved instance state")
    }

    override fun onActivityDestroyed(p0: Activity) {
        Log.d(TAG, "${p0.localClassName} now destroyed")
    }

}