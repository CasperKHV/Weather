package com.example.coloreffect

import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object WeatherSpec {

    fun getWeather(context: Context?, position: Int, weather: ModelForGSONWeatherClass): String {
        return """
               ${context!!.getString(R.string.now_in)}${weather.name}": ${weather.main!!.temp}${context.getString(R.string.celsius)}
               ${context.getString(R.string.for_wind_beggining)}${weather.wind!!.speed}${context.getString(R.string.for_wind)}
               ${weather.weather[0]!!.description}
               """.trimIndent()
    }

    fun getWeatherHistory(context: Context?, weather: ModelForGSONWeatherClass, currentDate: Date?): String {

        // Форматирование времени как "часы:минуты:секунды"
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeText = timeFormat.format(currentDate)
        return """
               ${context!!.getString(R.string.at)}$timeText${context.getString(R.string.it_was)}: ${weather.main!!.temp}${
            context.getString(
                R.string.celsius
            )
        }
               ${context.getString(R.string.for_wind_beggining)}${weather.wind!!.speed}${context.getString(R.string.for_wind)}
               ${weather.weather[0]!!.description}
               """.trimIndent()
    }

    fun getDate(context: Context?, currentDate: Date?): String {
        // Форматирование времени как "день.месяц.год"
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    fun getPressure(context: Context?, weather: ModelForGSONWeatherClass): String {
        return Math.round(weather.main!!.pressure / 1.333333333333).toString() + context!!.getString(R.string.mm_hg)
    }

    fun getFeels(context: Context?, weather: ModelForGSONWeatherClass): String {
        return context!!.getString(R.string.for_feels_beggining) + weather.main!!.feels_like
            .toString() + context.getString(R.string.celsius)
    }

    fun getHumidity(context: Context?, weather: ModelForGSONWeatherClass): String {
        return context!!.getString(R.string.humidity) + weather.main!!.humidity
            .toString() + context.getString(R.string.percent)
    }
}