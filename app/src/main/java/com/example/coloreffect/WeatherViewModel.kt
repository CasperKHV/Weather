package com.example.coloreffect

import android.util.Log
import androidx.lifecycle.ViewModel

class WeatherViewModel: ViewModel(){
    init {
        Log.d("WeatherViewModel","WeatherViewModel is created")
    }

    override fun onCleared() {
        Log.d("WeatherViewModel","onCleared")
    }
}