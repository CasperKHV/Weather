package com.example.coloreffect;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

final class WeatherSpec {

    private WeatherSpec() {
    }

    static String getWeather(Context context, int position, ModelForGSONWeatherClass weather) {
        return context.getString(R.string.now_in) + weather.getName() + "\": " + weather.main.temp + context.getString(R.string.celsius) + "\n" + context.getString(R.string.for_wind_beggining) + weather.wind.getSpeed() + context.getString(R.string.for_wind) + "\n" + weather.weather[0].getDescription();


    }

    static String getWeatherHistory(Context context, ModelForGSONWeatherClass weather, Date currentDate) {

        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        return context.getString(R.string.at) + timeText + context.getString(R.string.it_was) + ": " + weather.main.temp + context.getString(R.string.celsius) + "\n" + context.getString(R.string.for_wind_beggining) + weather.wind.getSpeed() + context.getString(R.string.for_wind) + "\n" + weather.weather[0].getDescription();


    }

    static String getDate(Context context, Date currentDate){
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    static String getPressure(Context context, ModelForGSONWeatherClass weather) {
        return String.valueOf(Math.round(weather.main.getPressure() / 1.333333333333)) + context.getString(R.string.mm_hg);


    }

    static String getFeels(Context context, ModelForGSONWeatherClass weather) {
        return context.getString(R.string.for_feels_beggining) + String.valueOf(weather.main.getFeels_like()) + context.getString(R.string.celsius);


    }

    static String getHumidity(Context context, ModelForGSONWeatherClass weather) {
        return context.getString(R.string.humidity) + String.valueOf(weather.main.getHumidity()) + context.getString(R.string.percent);


    }


}
