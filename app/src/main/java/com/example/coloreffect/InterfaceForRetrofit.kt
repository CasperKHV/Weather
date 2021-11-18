package com.example.coloreffect

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface InterfaceForRetrofit {

    @GET("weather")
    fun loadWeather(
        @Header("x-api-key") id: String?,
        @Query("q") city: String?,
        @Query("units") metric: String?,
        @Query("lang") language: String?
    ): Call<ModelForGSONWeatherClass?>?
}