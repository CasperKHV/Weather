package com.casperkhv.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

internal class FragmentLifecycleLoggingCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Log.d(TAG, "${f::class.simpleName} : PreAttached")
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Log.d(TAG, "${f::class.simpleName} : Attached")
    }

    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.d(TAG, "${f::class.simpleName} : PreCreated")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.d(TAG, "${f::class.simpleName} : Created")
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "${f::class.simpleName} : ViewCreated")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f::class.simpleName} : Started")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f::class.simpleName} : Resumed")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f::class.simpleName} : Paused")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f::class.simpleName} : Stopped")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Log.d(TAG, "${f::class.simpleName} : SaveInstanceState")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f::class.simpleName} : ViewDestroyed")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f::class.simpleName} : Destroyed")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f::class.simpleName} : Detached")
    }

    companion object {
        private const val TAG = "lifecycle/fragment"
    }
}