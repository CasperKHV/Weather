package com.example.coloreffect

import java.io.Serializable

class DataForBundle(
    var resultPressure: String?,
    var resultFeels: String?,
    var resultHumidity: String?,
    var message: String?,
    var dateForHistory: String?,
    var history: String?,
    var iconCode: String?,
    var photoWeather: Int,
    var city: String?
) : Serializable {

    companion object {

        private const val serialVersionUID = 1L
    }
}