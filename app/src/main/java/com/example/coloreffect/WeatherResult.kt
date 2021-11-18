package com.example.coloreffect

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.coloreffect.WeatherResultFragment

class WeatherResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate savedInstanceState$savedInstanceState")
        setContentView(R.layout.activity_weather_result)
        val resultFragment: WeatherResultFragment =
            WeatherResultFragment.newInstance(intent.getSerializableExtra(WeatherResultFragment.DATA_FOR_BUNDLE))
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, resultFragment)
        transaction.commit()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    companion object {

        private const val TAG = "### WeatherResult"
    }
}