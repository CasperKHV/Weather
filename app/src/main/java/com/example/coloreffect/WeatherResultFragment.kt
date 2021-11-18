package com.example.coloreffect;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;


public class WeatherResultFragment extends Fragment implements View.OnClickListener {

    private static final String INNER_FRAGMENT_TAG = "inner_fragment_tag";

    //блок для RecyclerView
    private NoteDataSourceForHistory noteDataSourceForHistory;     // Источник данных
    private NoteDataReaderForHistory noteDataReaderForHistory;      // Читатель данных
    private MyAdapter adapter;                // Адаптер для RecyclerView
    private HistoryListListener historyListListener;
    String city;
    String history;
    String dateForHistory;

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

        noteDataSourceForHistory = new NoteDataSourceForHistory(getActivity());

        RecyclerView citiesCategoriesRecyclerView = view.findViewById(R.id.recycler_view_history);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        citiesCategoriesRecyclerView.setLayoutManager(layoutManager);
//        citiesCategoriesRecyclerView.setAdapter(new MyAdapter());

        adapter = new MyAdapter();
        citiesCategoriesRecyclerView.setAdapter(adapter);

//        historyListListener.transfer(noteDataSourceForHistory, noteDataReaderForHistory, adapter);

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
            city = dataForBundle.getCity();
            history = dataForBundle.getHistory();
            dateForHistory = dataForBundle.getDateForHistory();
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

        if (city != null && dateForHistory != null && history != null) {
            initDataSource();
            Log.d("кол-во", Integer.toString(noteDataReaderForHistory.getCount()));
            Log.d("дата", dateForHistory);
            if (noteDataReaderForHistory.getCountForAvoidRepetition(city, dateForHistory) == 0) {
                noteDataSourceForHistory.addNote(dateForHistory, city, history);
            } else {
                noteDataSourceForHistory.editNote(dateForHistory, city, history);
            }
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

    //далее всё для Recycler View
    interface HistoryListListener {
        void onListItemClick(int id, DataForBundle dataForBundle, TextView descriptionText);

        void transfer(NoteDataSourceForHistory noteDataSourceForHistory, NoteDataReaderForHistory noteDataReaderForHistory, MyAdapter adapter);
    }


//    @Override
//    public void onAttach(Context context) {
//        try {
//            historyListListener = (HistoryListListener) context;
//        } catch (ClassCastException | NullPointerException e) {
//            throw new IllegalArgumentException(context.toString() + " must implement HistoryListListener");
//        }
//        super.onAttach(context);
//    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView categoryNameTextView;
        private ImageView photo;
        private TextView textNote;
        private HistoryNote note;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_history, parent, false));
            itemView.setOnClickListener(this);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.item_of_history_name_text_view);

// При долгом нажатии на элементе – вытащим  меню
            categoryNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopupMenu(categoryNameTextView);
                    return true;
                }
            });
            // при быстром нажатии откроем инфу о погоде в выбранном городе
            categoryNameTextView.setOnClickListener(this);
        }


//        void bind(int position) {
//            String category = getResources().getStringArray(R.array.cityes_selection)[position];
//            categoryNameTextView.setText(category);
//        }

        public void bind(HistoryNote note) {
            this.note = note;
            categoryNameTextView.setText(note.getDate());
        }

        @Override
        public void onClick(View view) {
            //showActivity(this.getLayoutPosition());

            // Выведем диалоговое окно для редактирования записи
            LayoutInflater factory = LayoutInflater.from(getActivity());
// alertView пригодится в дальнейшем для поиска пользовательских элементов
            final View alertView = factory.inflate(R.layout.layout_history_description, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(alertView);
//            builder.setTitle(note.getDate());
            TextView title = new TextView(getActivity());
// Customise your Title here
            title.setText(note.getDate());
            title.setBackgroundColor(Color.DKGRAY);
            title.setPadding(10, 10, 10, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(20);
            builder.setCustomTitle(title);


            TextView editTextNote = alertView.findViewById(R.id.textDescriptionHistory);



            // Если использовать findViewById без alertView, то всегда будем получать editText = null
            editTextNote.setText(note.getDescription());
            builder.setNegativeButton(R.string.Close, null);
            builder.show();

        }

        private void showPopupMenu(View view) {
// Покажем меню на элементе
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.context_menu_for_history, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                // Обработка выбора пункта меню
                @Override
                public boolean onMenuItemClick(MenuItem item) {
// Делегируем обработку слушателю
                    switch (item.getItemId()) {
                        case R.id.menu_for_history_delete:
                            deleteElement(note);
                            return true;
                        case R.id.menu_for_history_delete_all:
                            deleteHistoryForCity(city);
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
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
            holder.bind(noteDataReaderForHistory.getPosition(position));
        }

        @Override
        public int getItemCount() {
//            return getResources().getStringArray(R.array.cityes_selection).length;
            return noteDataReaderForHistory.getCount();
        }
    }

    private void deleteElement(HistoryNote note) {
        noteDataSourceForHistory.deleteNote(note);
        dataUpdated();
    }

    private void deleteHistoryForCity(String city){
        noteDataSourceForHistory.deleteHistoryForCity(city);
        dataUpdated();
    }

    private void dataUpdated() {
        noteDataReaderForHistory.Refresh(city);
        adapter.notifyDataSetChanged();
    }

    private void initDataSource() {
        noteDataSourceForHistory.open(city);
        noteDataReaderForHistory = noteDataSourceForHistory.getNoteDataReaderForHistory();
    }


}