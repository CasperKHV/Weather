package com.casperkhv.weather.temperature.domain

import com.casperkhv.weather.temperature.domain.model.TemperatureData

/**
 * Классы, которые реализуют бизнес-логику называются UseCase или Interactor.
 * Interactor и UseCase - это одна и та же сущность с точки зрения чистой архитектуры.
 */
internal interface TemperatureDataGetUseCase {

    fun getTemperatureData(city: String): TemperatureData
}