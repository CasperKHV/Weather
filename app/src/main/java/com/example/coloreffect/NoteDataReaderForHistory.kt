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
import android.database.Cursor
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import com.example.coloreffect.HistoryNote
import android.view.Gravity
import com.example.coloreffect.DatabaseHelperForHistory
import com.example.coloreffect.MainForGSON
import com.example.coloreffect.WeatherForGSON
import com.example.coloreffect.WindForGSON
import java.io.Closeable
import java.io.IOException

// Читатель источника данных на основе курсора
// Этот класс был вынесен из NoteDataSource, чтобы разгрузить его ответственности
class NoteDataReaderForHistory(  // но сами данные подсчитываются только по необходимости
    private val database: SQLiteDatabase?
) : Closeable {
    private var cursor // Курсор (фактически, подготовленный запрос),
            : Cursor? = null
    private val notesAllColumn = arrayOf<String>(
        DatabaseHelperForHistory.Companion.COLUMN_ID,
        DatabaseHelperForHistory.Companion.COLUMN_DATE,
        DatabaseHelperForHistory.Companion.COLUMN_NOTE,
        DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE
    )

    // Подготовить к чтению таблицу
    fun open(city: String?) {
        query(city)
        cursor!!.moveToFirst()
    }

    @Throws(IOException::class)
    override fun close() {
        cursor!!.close()
    }

    // Перечитать таблицу (если точно – обновить курсор)
    fun Refresh(city: String?) {
        val position = cursor!!.position
        query(city)
        cursor!!.moveToPosition(position)
    }

    // Создание запроса на курсор
    private fun query(city: String?) {
        cursor = database!!.query(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            notesAllColumn,
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE + "= ?",
            arrayOf(city),
            null,
            null,
            null
        )
    }

    // Создание запроса на курсор для определения, имеются ли данные по городу на определённую дату
    fun getCountForAvoidRepetition(city: String, date: String): Int {
        return database!!.query(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            notesAllColumn,
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE + "= ? AND " + DatabaseHelperForHistory.Companion.COLUMN_DATE + "= ?",
            arrayOf(city, date),
            null,
            null,
            null
        ).count
    }

    // Прочитать данные по определенной позиции
    fun getPosition(position: Int): HistoryNote {
        cursor!!.moveToPosition(position)
        return cursorToNote()
    }

    // Получить количество строк в таблице
    val count: Int
        get() = cursor!!.count

    // Преобразователь данных курсора в объект
    private fun cursorToNote(): HistoryNote {
        val note = HistoryNote()
        note.id = cursor!!.getLong(0)
        note.date = cursor!!.getString(1)
        note.description = cursor!!.getString(2)
        note.title = cursor!!.getString(3)
        return note
    }
}