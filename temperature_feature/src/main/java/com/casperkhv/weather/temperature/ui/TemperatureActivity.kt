package com.casperkhv.weather.temperature.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.casperkhv.weather.temperature.R

class TemperatureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TemperatureFragment.newInstance())
                .commitNow()
        }
    }
}