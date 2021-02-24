package com.example.coloreffect;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;


public class WeatherResultFragment extends Fragment implements View.OnClickListener {

    private static final String INNER_FRAGMENT_TAG = "inner_fragment_tag";

    private DataForBundle dataForBundle;
    private TextView weatherText;
    private Button shareButton;
    private String message;
    private ImageView photoWeather;
    public static final String DATA_FOR_BUNDLE = "data for bundle";

    public static WeatherResultFragment newInstance(Serializable dataForBundle) {
        WeatherResultFragment fragment = new WeatherResultFragment();
        fragment.dataForBundle = (DataForBundle) dataForBundle;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_result, container, false);
        photoWeather = view.findViewById(R.id.photoWeather);
        weatherText = view.findViewById(R.id.textview_weather);
        shareButton = view.findViewById(R.id.button_share);
        shareButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            dataForBundle = (DataForBundle) savedInstanceState.getSerializable(DATA_FOR_BUNDLE);
        }

        String photoWeatherCode;
        if (dataForBundle != null) {
            message = dataForBundle.getMessage();
            photoWeatherCode = dataForBundle.getIconCode();
        } else {
            throw new RuntimeException("DataForBundle is empty");
        }

        if (photoWeatherCode != null) {
            int imageId = R.drawable.troll_weather;
            switch (photoWeatherCode) {
                case "01d":
                    imageId = R.drawable.a01d;
                    break;
                case "01n":
                    imageId = R.drawable.a01n;
                    break;
                case "02d":
                    imageId = R.drawable.a02d;
                    break;
                case "02n":
                    imageId = R.drawable.a02n;
                    break;
                case "03d":
                    imageId = R.drawable.a03d;
                    break;
                case "03n":
                    imageId = R.drawable.a03n;
                    break;
                case "04d":
                    imageId = R.drawable.a04d;
                    break;
                case "04n":
                    imageId = R.drawable.a04n;
                    break;
                case "09d":
                    imageId = R.drawable.a09d;
                    break;
                case "09n":
                    imageId = R.drawable.a09n;
                    break;
                case "10d":
                    imageId = R.drawable.a10d;
                    break;
                case "10n":
                    imageId = R.drawable.a10n;
                    break;
                case "11d":
                    imageId = R.drawable.a11d;
                    break;
                case "11n":
                    imageId = R.drawable.a11n;
                    break;
                case "13d":
                    imageId = R.drawable.a13d;
                    break;
                case "13n":
                    imageId = R.drawable.a13n;
                    break;
                case "50d":
                    imageId = R.drawable.a50d;
                    break;
                case "50n":
                    imageId = R.drawable.a50n;
                    break;
                default:
                    break;

            }
            try {
                photoWeather.setImageResource(imageId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                photoWeather.setImageResource(R.drawable.troll_weather);
            }
            registerForContextMenu(photoWeather);
        }


        if (message != null) {
            weatherText.setText(message);
            Intent intentResult = new Intent();
            intentResult.putExtra(CitiesListFragment.RESULT_OK_STRING, getResources().getString(R.string.repeat_choose_city));
            getActivity().setResult(Activity.RESULT_OK, intentResult);
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        CheckBoxWeatherResultFragment checkBoxWeatherResultFragment = (CheckBoxWeatherResultFragment) fragmentManager.findFragmentByTag(INNER_FRAGMENT_TAG);
        if (checkBoxWeatherResultFragment == null) {
            String pressure = dataForBundle.getResultPressure();
            String feels = dataForBundle.getResultFeels();
            String humidity = dataForBundle.getResultHumidity();
            if (pressure != null || feels != null || humidity != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                checkBoxWeatherResultFragment = CheckBoxWeatherResultFragment.newInstance(pressure, feels, humidity);
                fragmentTransaction.replace(R.id.inner_fragment_container, checkBoxWeatherResultFragment, INNER_FRAGMENT_TAG);
                fragmentTransaction.commit();
            }
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_share) {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("text/plain");

            if (message != null) {
                intentShare.putExtra(Intent.EXTRA_TEXT, message);
            }
            PackageManager packageManager = getActivity().getPackageManager();

            if (!packageManager.queryIntentActivities(intentShare, 0).isEmpty()) {
                startActivity(intentShare);
                shareButton.setBackgroundColor(Color.GREEN);
            } else {
                shareButton.setBackgroundColor(Color.RED);
            }
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu_hide:
                photoWeather.setVisibility(View.GONE);
                return true;

            case R.id.context_menu_set_background:
                photoWeather.setBackgroundColor(Color.BLUE);
                return true;

            case R.id.context_menu_delete_background:
                photoWeather.setBackgroundColor(Color.TRANSPARENT);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (dataForBundle != null) {
            outState.putSerializable(DATA_FOR_BUNDLE, dataForBundle);
        }
        super.onSaveInstanceState(outState);
    }
}