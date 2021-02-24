package com.example.coloreffect;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class CheckBoxWeatherResultFragment extends Fragment {

    TextView pressureTextView;
    TextView feelsTextView;
    TextView humidityTextView;
    String pressure;
    String feels;
    String humidity;

    public static CheckBoxWeatherResultFragment newInstance(String pressure, String feels, String humidity) {
        CheckBoxWeatherResultFragment fragment = new CheckBoxWeatherResultFragment();
        fragment.pressure = pressure;
        fragment.feels = feels;
        fragment.humidity = humidity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_box_weather_result, container, false);
        pressureTextView = view.findViewById(R.id.textview_pressure);
        feelsTextView = view.findViewById(R.id.textview_feels);
        humidityTextView = view.findViewById(R.id.textview_humidity);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (pressure != null) {
            pressureTextView.setVisibility(View.VISIBLE);
            pressureTextView.setText(pressure);
        }

        if (feels != null) {
            feelsTextView.setVisibility(View.VISIBLE);
            feelsTextView.setText(feels);
        }

        if (humidity != null) {
            humidityTextView.setVisibility(View.VISIBLE);
            humidityTextView.setText(humidity);
        }


    }


}