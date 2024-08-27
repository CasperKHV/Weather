package com.casperkhv.weather.temperature.domain

import com.casperkhv.weather.temperature.domain.model.TemperatureData

/**
 * Классы, которые реализуют контракты взаимодействия domain слоя с data слоем называются Repository.
 */
internal interface TemperatureRepository {

    fun getTemperatureData(city: String): TemperatureData
}