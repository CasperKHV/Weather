package com.example.coloreffect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CitiesListFragment extends Fragment {

    private boolean errorCode;

    private NoteDataSource notesDataSource;     // Источник данных
    private NoteDataReader noteDataReader;      // Читатель данных
    private MyAdapter adapter;                // Адаптер для RecyclerView


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

        void transfer(NoteDataSource notesDataSourceNoteDataSource, NoteDataReader noteDataReader, MyAdapter adapter);
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

        initDataSource();

        RecyclerView citiesCategoriesRecyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        citiesCategoriesRecyclerView.setLayoutManager(layoutManager);
//        citiesCategoriesRecyclerView.setAdapter(new MyAdapter());

        adapter = new MyAdapter();
        citiesCategoriesRecyclerView.setAdapter(adapter);

//        adapter = new NoteAdapter(noteDataReader);
//        adapter.setOnMenuItemClickListener(new NoteAdapter.OnMenuItemClickListener() {
//            @Override
//            public void onItemEditClick(CityNote note) {
//                editElement(note);
//            }
//
//            @Override
//            public void onItemDeleteClick(CityNote note) {
//                deleteElement(note);
//            }
//        });
//        citiesCategoriesRecyclerView.setAdapter(adapter);
        citiesListListener.transfer(notesDataSource, noteDataReader, adapter);
        if (noteDataReader.getCount() == 0) {
            initCities();
        }


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

        private TextView categoryNameTextView;
        private ImageView photo;
        private TextView textNote;
        private CityNote note;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.category_list_item, parent, false));
            itemView.setOnClickListener(this);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.category_name_text_view);
        }


//        void bind(int position) {
//            String category = getResources().getStringArray(R.array.cityes_selection)[position];
//            categoryNameTextView.setText(category);
//        }

        public void bind(CityNote note) {
            this.note = note;
            categoryNameTextView.setText(note.getTitle());
        }

        @Override
        public void onClick(View view) {
            showActivity(this.getLayoutPosition());
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MyViewHolder(inflater, parent);
        }

//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//// Создаем новый элемент пользовательского интерфейса
//// Через Inflater
//            View v = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.category_list_item, parent, false);
//// Здесь можно установить всякие параметры
//            MyViewHolder vh = new MyViewHolder(inf);
//            return vh;
//        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.bind(position);
            holder.bind(noteDataReader.getPosition(position));
        }

        @Override
        public int getItemCount() {
//            return getResources().getStringArray(R.array.cityes_selection).length;
            return noteDataReader.getCount();
        }
    }

    private void showActivity(int categoryId) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Controller controller = new Controller();
                String resultPressure = null;
                String resultFeels = null;
                String resultHumidity = null;
                String iconCode = null;
                String city = noteDataReader.getPosition(categoryId).getDescription();
                savedCity.edit().putInt(PREVIOUS_WEATHER_ID, categoryId).apply();
                String[] cityNamesForAPI = getResources().getStringArray(R.array.city_names_for_load_weather);
//                ModelForGSONWeatherClass weather = controller.start(getActivity(), cityNamesForAPI[categoryId]);
                ModelForGSONWeatherClass weather = controller.start(getActivity(), city);
                if (weather == null) {
                    errorCode = true;
                    return;
                }
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
        };
        thread.start();
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if (errorCode) {
            errorCode = false;
            Toast.makeText(getActivity(), "City not found", Toast.LENGTH_SHORT).show();
        }
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

    private void editElement(CityNote note) {
        notesDataSource.editNote(note, "Edited", "Edited title");
        dataUpdated();
    }

    private void deleteElement(CityNote note) {
        notesDataSource.deleteNote(note);
        dataUpdated();
    }

    private void dataUpdated() {
        noteDataReader.Refresh();
        adapter.notifyDataSetChanged();
    }

    private void initDataSource() {
        notesDataSource = new NoteDataSource(getActivity());
        notesDataSource.open();
        noteDataReader = notesDataSource.getNoteDataReader();
    }

    private void initCities() {
        notesDataSource.addNote(getActivity().getResources().getStringArray(R.array.cityes_selection)[0], getActivity().getResources().getStringArray(R.array.city_names_for_load_weather)[0]);
        notesDataSource.addNote(getActivity().getResources().getStringArray(R.array.cityes_selection)[1], getActivity().getResources().getStringArray(R.array.city_names_for_load_weather)[1]);
        notesDataSource.addNote(getActivity().getResources().getStringArray(R.array.cityes_selection)[2], getActivity().getResources().getStringArray(R.array.city_names_for_load_weather)[2]);
        dataUpdated();
    }


}