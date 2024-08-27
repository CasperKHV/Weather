package com.casperkhv.weather.temperature.presentation.model

/**
 * Часто на presentation слое создают отдельные модели данных, которые удобно
 * использовать для отображения UI.
 */
internal data class TemperatureDataUiModel(
    val temperature: String,
)