package com.example.coloreffect

class ModelForGSONWeatherClass {
    var base: String? = null
    var name: String? = null
    var main: MainForGSON? = null
    lateinit var weather: Array<WeatherForGSON?>
    var wind: WindForGSON? = null
}