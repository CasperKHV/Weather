package com.example.coloreffect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CitiesListFragment extends Fragment {


    public static final String SAVED_CITY = "savedCity";
    private SharedPreferences savedCity;
    public static final String RESULT_OK_STRING = "Ok";

    private CheckBox checkBoxPressure;
    private CheckBox checkBoxFeels;
    private CheckBox checkBoxHumidity;

    private static final String CHECK_BOX_PRESSURE = "checkBoxPressure";
    private static final String CHECK_BOX_FEELS = "checkBoxFeels";
    private static final String CHECK_BOX_HUMIDITY = "checkBoxWeek";
    public static final String PREVIOUS_WEATHER_ID = "previous weather id";

    public static final int REQUEST_CODE = 100;
    private TextView descriptionText;

    private CitiesListListener citiesListListener;

    interface CitiesListListener {
        void onListItemClick(int id, DataForBundle dataForBundle, TextView descriptionText);
    }


    @Override
    public void onAttach(Context context) {
        try {
            citiesListListener = (CitiesListListener) context;
        } catch (ClassCastException | NullPointerException e) {
            throw new IllegalArgumentException(context.toString() + " must implement CitiesListListener");
        }
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cities_list, container, false);
        RecyclerView cityesCategoriesRecyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cityesCategoriesRecyclerView.setLayoutManager(layoutManager);
        cityesCategoriesRecyclerView.setAdapter(new MyAdapter());


        initializeViews(rootView);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializePreferences();
        checkBoxPressure.setChecked(savedCity.getBoolean(CHECK_BOX_PRESSURE, false));
        checkBoxFeels.setChecked(savedCity.getBoolean(CHECK_BOX_FEELS, false));
        checkBoxHumidity.setChecked(savedCity.getBoolean(CHECK_BOX_HUMIDITY, false));
        int previousWeatherId = savedCity.getInt(PREVIOUS_WEATHER_ID, -1);
        if (previousWeatherId != -1) {
            showActivity(previousWeatherId);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int[] ID = new int[]{R.drawable.khv_image, R.drawable.spb_image, R.drawable.moscow_image};
        private TextView categoryNameTextView;
        private ImageView photo;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.category_list_item, parent, false));
            itemView.setOnClickListener(this);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.category_name_text_view);
            photo = itemView.findViewById(R.id.photo);
        }

        void bind(int position) {
            String category = getResources().getStringArray(R.array.cityes_selection)[position];
            categoryNameTextView.setText(category);
            photo.setImageResource(ID[position]);
        }

        @Override
        public void onClick(View view) {
            showActivity(this.getLayoutPosition());
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return getResources().getStringArray(R.array.cityes_selection).length;
        }
    }

    private void showActivity(int categoryId) {
        new Thread() {
            public void run() {
                Controller controller = new Controller();
                String resultPressure = null;
                String resultFeels = null;
                String resultHumidity = null;
                String iconCode = null;
                savedCity.edit().putInt(PREVIOUS_WEATHER_ID, categoryId).apply();
                String[] cityNamesForAPI = getResources().getStringArray(R.array.city_names);
                ModelForGSONWeatherClass weather = controller.start(getActivity(), cityNamesForAPI[categoryId]);

                String resultWeather = WeatherSpec.getWeather(getActivity(), categoryId, weather);
                if (checkBoxPressure.isChecked()) {

                    if (weather != null) {
                        resultPressure = WeatherSpec.getPressure(getActivity(), weather);
                    }
                }

                if (checkBoxFeels.isChecked()) {
                    resultFeels = WeatherSpec.getFeels(getActivity(), weather);
                }

                if (checkBoxHumidity.isChecked()) {
                    resultHumidity = WeatherSpec.getHumidity(getActivity(), weather);
                }

                iconCode = weather.weather[0].getIcon();
                DataForBundle dataForBundle = new DataForBundle(resultPressure, resultFeels, resultHumidity, resultWeather, iconCode, categoryId);
                citiesListListener.onListItemClick(categoryId, dataForBundle, descriptionText);
            }
        }.start();
    }

    private void initializeViews(View view) {
        descriptionText = view.findViewById(R.id.textview_description);
        checkBoxPressure = view.findViewById(R.id.checkbox_pressure);
        checkBoxFeels = view.findViewById(R.id.checkbox_feels);
        checkBoxHumidity = view.findViewById(R.id.checkbox_humidity);


    }


    private void initializePreferences() {
        savedCity = getActivity().getSharedPreferences(SAVED_CITY, Context.MODE_PRIVATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        savedCity.edit().putBoolean(CHECK_BOX_PRESSURE, checkBoxPressure.isChecked()).apply();
        savedCity.edit().putBoolean(CHECK_BOX_FEELS, checkBoxFeels.isChecked()).apply();
        savedCity.edit().putBoolean(CHECK_BOX_HUMIDITY, checkBoxHumidity.isChecked()).apply();
        super.onSaveInstanceState(outState);
    }


}