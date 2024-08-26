package com.casperkhv.weather.temperature.domain

import com.casperkhv.weather.temperature.domain.model.TemperatureData

internal class TemperatureDataUseCaseImpl(
    private val temperatureRepository: TemperatureRepository,
) : TemperatureDataGetUseCase {

    override fun getTemperatureData(): TemperatureData {
        return temperatureRepository.getTemperatureData()
    }
}