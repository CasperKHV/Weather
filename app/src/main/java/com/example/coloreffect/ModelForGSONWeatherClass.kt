package com.example.coloreffect

class ModelForGSONWeatherClass {

    var base: String? = null
    var name: String? = null
    var main: MainForGSON? = null
    var weather: Array<WeatherForGSON?> = emptyArray()
    var wind: WindForGSON? = null
}