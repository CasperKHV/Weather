package com.example.coloreffect;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller implements Callback<ModelForGSONWeatherClass> {

    private ModelForGSONWeatherClass weather;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String OPEN_API_KEY = "6025d97aacdbfdafb3a5676d96e6710f";

    public ModelForGSONWeatherClass start(Context context, String city) {
        Gson gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        InterfaceForRetrofit interfaceForRetrofit = retrofit.create(InterfaceForRetrofit.class);
        Call<ModelForGSONWeatherClass> call = interfaceForRetrofit.loadWeather(OPEN_API_KEY, city, "metric", context.getString(R.string.language));
        try {
            Response<ModelForGSONWeatherClass> response = call.execute();
            weather = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;

    }


    @Override
    public void onResponse(Call<ModelForGSONWeatherClass> call, Response<ModelForGSONWeatherClass> response) {
        Log.d("code", String.valueOf(response.code()));
        if (response.isSuccessful() && response.body() != null) {
            weather = response.body();
            Log.d("BASE", weather.base);
            Log.d("Pressure", String.valueOf(weather.main.getPressure()));
            Log.d("main", String.valueOf(weather.weather[0].main));
        } if (response.body() != null){
            Log.d("Error with response", "response.body is empty");
        }else {
            Log.d("Error with response", response.message());
        }
    }

    @Override
    public void onFailure(Call<ModelForGSONWeatherClass> call, Throwable t) {
        t.printStackTrace();
    }
}
