package com.example.coloreffect;

import java.io.Serializable;

public class DataForBundle implements Serializable {

    private static final long serialVersionUID = 1L;

    String resultPressure;
    String resultFeels;
    String resultHumidity;
    String message;
    String iconCode;
    int photoWeather;

    public DataForBundle(String resultPressure, String resultFeels, String resultHumidity, String message, String iconCode, int photoWeather) {
        this.resultPressure = resultPressure;
        this.resultFeels = resultFeels;
        this.resultHumidity = resultHumidity;
        this.message = message;
        this.iconCode = iconCode;
        this.photoWeather = photoWeather;
    }

    public String getResultPressure() {
        return resultPressure;
    }

    public void setResultPressure(String resultPressure) {
        this.resultPressure = resultPressure;
    }

    public String getResultFeels() {
        return resultFeels;
    }

    public void setResultFeels(String resultFeels) {
        this.resultFeels = resultFeels;
    }

    public String getResultHumidity() {
        return resultHumidity;
    }

    public void setResultHumidity(String resultHumidity) {
        this.resultHumidity = resultHumidity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPhotoWeather() {
        return photoWeather;
    }

    public void setPhotoWeather(int photoWeather) {
        this.photoWeather = photoWeather;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }
}
