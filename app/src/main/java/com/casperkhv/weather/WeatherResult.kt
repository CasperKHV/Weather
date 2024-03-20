package com.casperkhv.weather

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

internal class WeatherResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_result)
        val resultFragment: WeatherResultFragment = WeatherResultFragment.Companion.newInstance(
            intent.getSerializableExtra(WeatherResultFragment.Companion.DATA_FOR_BUNDLE)
        )
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, resultFragment)
        transaction.commit()
    }

    companion object {
        private const val TAG = "### WeatherResult"
    }
}