package com.example.coloreffect

//import com.example.coloreffect.Controller.start
import com.example.coloreffect.NoteDataReader
import androidx.recyclerview.widget.RecyclerView
import com.example.coloreffect.R
import com.example.coloreffect.CityNote
import android.widget.TextView
import com.example.coloreffect.ModelForGSONWeatherClass
import androidx.appcompat.app.AppCompatActivity
import com.example.coloreffect.CitiesListFragment.CitiesListListener
import androidx.navigation.ui.AppBarConfiguration
import com.example.coloreffect.NoteDataSource
import android.os.Bundle
import com.example.coloreffect.MainActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.coloreffect.DataForBundle
import com.example.coloreffect.WeatherResultFragment
import android.content.Intent
import com.example.coloreffect.WeatherResult
import com.example.coloreffect.CitiesListFragment
import android.content.SharedPreferences
import android.content.DialogInterface
import android.widget.EditText
import androidx.core.view.GravityCompat
import com.example.coloreffect.FragmentForNV
import android.os.Environment
import com.bumptech.glide.Glide
import android.database.sqlite.SQLiteOpenHelper
import com.example.coloreffect.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import kotlin.Throws
import android.content.ContentValues
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View.OnLongClickListener
import com.example.coloreffect.WeatherSpec
import android.widget.Toast
import retrofit2.http.GET
import com.example.coloreffect.NoteDataSourceForHistory
import com.example.coloreffect.NoteDataReaderForHistory
import com.example.coloreffect.WeatherResultFragment.HistoryListListener
import android.content.res.Resources.NotFoundException
import android.app.Activity
import com.example.coloreffect.CheckBoxWeatherResultFragment
import android.content.pm.PackageManager
import android.util.Log
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import androidx.appcompat.app.AlertDialog
import com.example.coloreffect.HistoryNote
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import com.example.coloreffect.DatabaseHelperForHistory
import com.example.coloreffect.MainForGSON
import com.example.coloreffect.WeatherForGSON
import com.example.coloreffect.WindForGSON

class MainActivity : AppCompatActivity(), CitiesListListener {
    private var descriptionText: TextView? = null
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var notesDataSource // Источник данных
            : NoteDataSource? = null
    private var noteDataReader // Читатель данных
            : NoteDataReader? = null
    private var adapter // Адаптер для RecyclerView
            : CitiesListFragment.MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate savedInstanceState$savedInstanceState")
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_list_fragment, R.id.nav_fragment_for_n_v
        )
            .setDrawerLayout(drawer)
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onListItemClick(
        id: Int,
        dataForBundle: DataForBundle?,
        descriptionText: TextView?
    ) {
        this.descriptionText = descriptionText
        val fragmentContainer = findViewById<View>(R.id.fragment_container_land)
        if (fragmentContainer != null) {
            val weatherResultFragment: WeatherResultFragment =
                WeatherResultFragment.Companion.newInstance(dataForBundle)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            transaction.replace(R.id.fragment_container_land, weatherResultFragment)
            transaction.commit()
        } else {
            val intent = Intent(this, WeatherResult::class.java)
            intent.putExtra(WeatherResultFragment.Companion.DATA_FOR_BUNDLE, dataForBundle)
            startActivityForResult(intent, CitiesListFragment.Companion.REQUEST_CODE)
        }
    }

    override fun transfer(
        notesDataSourceNoteDataSource: NoteDataSource?,
        noteDataReader: NoteDataReader?,
        adapter: CitiesListFragment.MyAdapter?
    ) {
        notesDataSource = notesDataSourceNoteDataSource
        this.noteDataReader = noteDataReader
        this.adapter = adapter
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (descriptionText != null) {
            if (requestCode == CitiesListFragment.Companion.REQUEST_CODE && data != null) {
                descriptionText!!.text =
                    data.getStringExtra(CitiesListFragment.Companion.RESULT_OK_STRING)
            }
            val sharedPreferences =
                getSharedPreferences(CitiesListFragment.Companion.SAVED_CITY, MODE_PRIVATE)
            sharedPreferences.edit().putInt(CitiesListFragment.Companion.PREVIOUS_WEATHER_ID, -1)
                .apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                addElement()
                true
            }
            R.id.menu_clear -> {
                clearList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearList() {
        notesDataSource!!.deleteAll()
        dataUpdated()
    }

    private fun addElement() {
// Выведем диалоговое окно для ввода новой записи
        val factory = LayoutInflater.from(this)
        // alertView пригодится в дальнейшем для поиска пользовательских элементов
        val alertView = factory.inflate(R.layout.layout_add_city_note, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(alertView)
        builder.setTitle(R.string.alert_title_add)
        builder.setNegativeButton(R.string.alert_cancel, null)
        builder.setPositiveButton(R.string.menu_add) { dialog, id ->
            val editTextNote = alertView.findViewById<EditText>(R.id.editTextNote)
            val editTextNoteTitle = alertView.findViewById<EditText>(R.id.editTextNoteTitle)
            // Если использовать findViewById без alertView, то всегда будем получать editText = null
            notesDataSource!!.addNote(
                editTextNoteTitle.text.toString(),
                editTextNote.text.toString()
            )
            dataUpdated()
        }
        builder.show()
    }

    private fun dataUpdated() {
        noteDataReader!!.Refresh()
        adapter!!.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    companion object {
        private const val TAG = "### MainActivity"
    }
}