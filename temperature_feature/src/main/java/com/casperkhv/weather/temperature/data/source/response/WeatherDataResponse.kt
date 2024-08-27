package com.casperkhv.weather.temperature.data.source.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Это DTO - Data Transfer Object. Объект, который мы ожидаем с сервера.
 * Аннотации относятся к библиотеке kotlinx.serialization.
 */
@Serializable
data class WeatherDataResponse(
    @SerialName("base")
    val base: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("main")
    val main: MainWeatherData,
    @SerialName("additionalWeatherData")
    val additionalWeatherData: List<AdditionalWeatherData>?,
    @SerialName("windInfo")
    val windInfo: WindInfo?,
) {

    @Serializable
    data class MainWeatherData(
        @SerialName("temp")
        val temp: Float,
        @SerialName("feelsLike")
        val feelsLike: Float?,
        @SerialName("tempMin")
        val tempMin: Float?,
        @SerialName("tempMax")
        val tempMax: Float?,
        @SerialName("pressure")
        val pressure: Int?,
        @SerialName("humidity")
        val humidity: Int?,
    )

    @Serializable
    data class AdditionalWeatherData(
        @SerialName("id")
        val id: Int,
        @SerialName("main")
        val main: String?,
        @SerialName("description")
        val description: String?,
        @SerialName("icon")
        val icon: String?,
    )

    @Serializable
    data class WindInfo(
        @SerialName("speed")
        val speed: Float?,
        @SerialName("direction")
        val direction: Int?,
    )
}