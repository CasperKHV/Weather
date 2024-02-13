package com.example.coloreffect

//import com.example.coloreffect.Controller.start
import com.example.coloreffect.NoteDataReader
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.coloreffect.R
import com.example.coloreffect.CityNote
import android.widget.TextView
import android.view.MenuInflater
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
import com.example.coloreffect.WeatherResult
import com.example.coloreffect.CitiesListFragment
import android.widget.EditText
import androidx.core.view.GravityCompat
import com.example.coloreffect.FragmentForNV
import android.os.Environment
import com.bumptech.glide.Glide
import android.database.sqlite.SQLiteOpenHelper
import com.example.coloreffect.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import kotlin.Throws
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
import android.content.*
import com.example.coloreffect.CheckBoxWeatherResultFragment
import android.content.pm.PackageManager
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import com.example.coloreffect.HistoryNote
import android.view.Gravity
import com.example.coloreffect.DatabaseHelperForHistory
import com.example.coloreffect.MainForGSON
import com.example.coloreffect.WeatherForGSON
import com.example.coloreffect.WindForGSON

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Вызывается при попытке доступа к базе данных, когда она еще не создана
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TABLE_NOTES + " (" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NOTE
                    + " TEXT," + COLUMN_NOTE_TITLE + " TEXT);"
        )
    }

    // Вызывается, когда необходимо обновление базы данных
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion == 2) {
            val upgradeQuery =
                "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_NOTE_TITLE + " TEXT DEFAULT 'Title'"
            db.execSQL(upgradeQuery)
        }
    }

    companion object {
        private const val DATABASE_NAME = "cities.db" // Название БД
        const val DATABASE_VERSION = 2 // Версия базы данных
        const val TABLE_NOTES = "cities" // Название таблицы в БД

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_NOTE_TITLE = "title"
        const val COLUMN_NOTE = "note"
    }
}