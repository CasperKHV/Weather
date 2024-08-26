package com.casperkhv.weather.temperature.domain.model

/**
 * В Android разработке модели данных доменного слоя часто называют Entity, хотя с точки зрения чистой
 * архитектуры entity - это отдельный слой, содержащий бизнес-логику уровня предприятия, то есть
 * бизнес-логику, которую  используют несколько приложений.
 */
internal data class TemperatureData(
    val temperature: Float,
)
