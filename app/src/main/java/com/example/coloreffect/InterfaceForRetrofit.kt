package com.example.coloreffect;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface InterfaceForRetrofit {

    @GET("weather")
    Call<ModelForGSONWeatherClass> loadWeather(@Header("x-api-key") String id, @Query("q") String city, @Query("units") String metric, @Query("lang") String language);
}
