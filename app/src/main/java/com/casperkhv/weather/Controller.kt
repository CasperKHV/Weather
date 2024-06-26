package com.casperkhv.weather

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Controller : Callback<ModelForGSONWeatherClass?> {
    private var weather: ModelForGSONWeatherClass? = null
    fun start(context: Context, city: String?): ModelForGSONWeatherClass? {
        val gson = Gson()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val interfaceForRetrofit = retrofit.create(
            InterfaceForRetrofit::class.java
        )
        val call = interfaceForRetrofit.loadWeather(
            OPEN_API_KEY,
            city,
            "metric",
            context.getString(R.string.language)
        )
        try {
            val response = call?.execute()
            if (response != null) {
                if (response.code() != SUCCESS_RESPONSE_CODE) {
                    return null
                }
                weather = response.body()
            }
        } catch (e: IOException) {
            Log.e("Controller", "It seems that the try to read or write failed", e)
        }
        return weather
    }

    override fun onResponse(
        call: Call<ModelForGSONWeatherClass?>,
        response: Response<ModelForGSONWeatherClass?>
    ) {
        Log.d("code", response.code().toString())
        if (response.isSuccessful && response.body() != null) {
            weather = response.body()
            Log.d("Base", weather?.base.toString())
            Log.d("Pressure", weather!!.main?.pressure.toString())
            Log.d("main", weather!!.weather[0]?.main.toString())
        }
        if (response.body() != null) {
            Log.d("Error with response", "response.body is empty")
        } else {
            Log.d("Error with response", response.message())
        }
    }

    override fun onFailure(call: Call<ModelForGSONWeatherClass?>, t: Throwable) {
        t.printStackTrace()
    }

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val OPEN_API_KEY = "6025d97aacdbfdafb3a5676d96e6710f"
        private const val SUCCESS_RESPONSE_CODE = 200
    }
}