package com.casperkhv.weather.temperature.data

import com.casperkhv.weather.temperature.data.source.TemperatureService
import com.casperkhv.weather.temperature.domain.TemperatureRepository
import com.casperkhv.weather.temperature.domain.model.TemperatureData
import kotlinx.coroutines.runBlocking

internal class TemperatureRepositoryImpl(
    private val temperatureService: TemperatureService,
) : TemperatureRepository {

    /**
     * runBlocking позволяет просто запустить корутину с блокировкой потока из которого
     * был сделан вызов. Это временное решение.
     */
    override fun getTemperatureData(city: String): TemperatureData {
        val response = runBlocking {
            temperatureService.loadWeatherData(
                apiKey = "6025d97aacdbfdafb3a5676d96e6710f",
                city = city,
                metric = "metric",
                language = "ru",
            )
        }

        return TemperatureData(
            temperature = response.main.temp,
        )
    }
}