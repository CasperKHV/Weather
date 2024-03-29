package com.casperkhv.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

internal class FragmentLifecycleLoggingCallback : FragmentManager.FragmentLifecycleCallbacks() {

    private val Fragment.simpleName
        get() = this::class.simpleName

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Log.d(TAG, "${f.simpleName} : PreAttached")
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Log.d(TAG, "${f.simpleName} : Attached")
    }

    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.d(TAG, "${f.simpleName} : PreCreated")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.d(TAG, "${f.simpleName} : Created")
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "${f.simpleName} : ViewCreated")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f.simpleName} : Started")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f.simpleName} : Resumed")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f.simpleName} : Paused")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f.simpleName} : Stopped")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Log.d(TAG, "${f.simpleName} : SaveInstanceState")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f.simpleName} : ViewDestroyed")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f.simpleName} : Destroyed")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Log.d(TAG, "${f.simpleName} : Detached")
    }

    companion object {
        private const val TAG = "lifecycle/fragment"
    }
}