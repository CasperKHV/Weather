package com.example.coloreffect;

public class ModelForGSONWeatherClass {
    String base;
    MainForGSON main;
    WeatherForGSON[] weather;
    WindForGSON wind;


    public ModelForGSONWeatherClass() {
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public MainForGSON getMain() {
        return main;
    }

    public void setMain(MainForGSON main) {
        this.main = main;
    }

    public WeatherForGSON[] getWeather() {
        return weather;
    }

    public void setWeather(WeatherForGSON[] weather) {
        this.weather = weather;
    }

    public WindForGSON getWind() {
        return wind;
    }

    public void setWind(WindForGSON wind) {
        this.wind = wind;
    }
}
