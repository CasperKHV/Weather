package com.casperkhv.weather.temperature.data.source

import com.casperkhv.weather.temperature.data.source.response.WeatherDataResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface TemperatureService {

    /**
     * Модификатор suspend говорит Retrofit использовать корутиры как Call адаптер.
     * Таким образом мы можем вернуть из метода просто класс, не оборачивая его в объект Call.
     */
    @GET("weather")
    suspend fun loadWeatherData(
        @Header("x-api-key") apiKey: String,
        @Query("q") city: String,
        @Query("units") metric: String,
        @Query("lang") language: String,
    ): WeatherDataResponse
}