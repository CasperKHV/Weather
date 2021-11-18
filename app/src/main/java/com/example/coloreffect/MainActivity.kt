package com.example.coloreffect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements CitiesListFragment.CitiesListListener {

    private static final String TAG = "### MainActivity";
    private TextView descriptionText;

    private AppBarConfiguration mAppBarConfiguration;

    private NoteDataSource notesDataSource;     // Источник данных
    private NoteDataReader noteDataReader;      // Читатель данных
    private CitiesListFragment.MyAdapter adapter;                // Адаптер для RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate savedInstanceState" + savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_list_fragment, R.id.nav_fragment_for_n_v)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onListItemClick(int id, DataForBundle dataForBundle, TextView descriptionText) {
        this.descriptionText = descriptionText;
        View fragmentContainer = findViewById(R.id.fragment_container_land);
        if (fragmentContainer != null) {
            WeatherResultFragment weatherResultFragment = WeatherResultFragment.newInstance(dataForBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_container_land, weatherResultFragment);
            transaction.commit();
        } else {
            Intent intent = new Intent(this, WeatherResult.class);
            intent.putExtra(WeatherResultFragment.DATA_FOR_BUNDLE, dataForBundle);
            startActivityForResult(intent, CitiesListFragment.REQUEST_CODE);

        }

    }

    @Override
    public void transfer(NoteDataSource notesDataSourceNoteDataSource, NoteDataReader noteDataReader, CitiesListFragment.MyAdapter adapter) {
        this.notesDataSource = notesDataSourceNoteDataSource;
        this.noteDataReader = noteDataReader;
        this.adapter = adapter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (descriptionText != null) {
            if (requestCode == CitiesListFragment.REQUEST_CODE && data != null) {
                descriptionText.setText(data.getStringExtra(CitiesListFragment.RESULT_OK_STRING));
            }
            SharedPreferences sharedPreferences = getSharedPreferences(CitiesListFragment.SAVED_CITY, MODE_PRIVATE);
            sharedPreferences.edit().putInt(CitiesListFragment.PREVIOUS_WEATHER_ID, -1).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                addElement();
                return true;
            case R.id.menu_clear:
                clearList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearList() {
        notesDataSource.deleteAll();
        dataUpdated();
    }

    private void addElement() {
// Выведем диалоговое окно для ввода новой записи
        LayoutInflater factory = LayoutInflater.from(this);
// alertView пригодится в дальнейшем для поиска пользовательских элементов
        final View alertView = factory.inflate(R.layout.layout_add_city_note, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView);
        builder.setTitle(R.string.alert_title_add);
        builder.setNegativeButton(R.string.alert_cancel, null);
        builder.setPositiveButton(R.string.menu_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText editTextNote = alertView.findViewById(R.id.editTextNote);
                EditText editTextNoteTitle = alertView.findViewById(R.id.editTextNoteTitle);
// Если использовать findViewById без alertView, то всегда будем получать editText = null
                notesDataSource.addNote(editTextNoteTitle.getText().toString(), editTextNote.getText().toString());
                dataUpdated();
            }
        });
        builder.show();
    }


    private void dataUpdated() {
        noteDataReader.Refresh();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
