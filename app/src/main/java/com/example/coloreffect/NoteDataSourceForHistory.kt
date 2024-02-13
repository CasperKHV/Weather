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
import android.database.SQLException
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

//  Источник данных, позволяет изменять данные в таблице
// Создает и держит в себе читатель данных
class NoteDataSourceForHistory(context: Context?) : Closeable {
    private val dbHelper: DatabaseHelperForHistory
    private var database: SQLiteDatabase? = null

    // Вернуть читателя (он потребуется в других местах)
    var noteDataReaderForHistory: NoteDataReaderForHistory? = null
        private set

    // Открывает базу данных
    @Throws(SQLException::class)
    fun open(city: String?) {
        database = dbHelper.writableDatabase
        // Создать читателя и открыть его
        noteDataReaderForHistory = NoteDataReaderForHistory(database)
        noteDataReaderForHistory!!.open(city)
    }

    @Throws(IOException::class)
    override fun close() {
        noteDataReaderForHistory!!.close()
        dbHelper.close()
    }

    // Добавить новую запись
    fun addNote(date: String?, title: String?, description: String?): HistoryNote {
        val values = ContentValues()
        values.put(DatabaseHelperForHistory.Companion.COLUMN_DATE, date)
        values.put(DatabaseHelperForHistory.Companion.COLUMN_NOTE, description)
        values.put(DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE, title)
        // Добавление записи
        val insertId =
            database!!.insert(DatabaseHelperForHistory.Companion.TABLE_NOTES, null, values)
        val note = HistoryNote()
        note.id = insertId
        note.date = date
        note.title = title
        note.description = description
        return note
    }

    // Изменить запись
    fun editNote(date: String, title: String, description: String?) {
        val editedNote = ContentValues()
        editedNote.put(DatabaseHelperForHistory.Companion.COLUMN_DATE, date)
        editedNote.put(DatabaseHelperForHistory.Companion.COLUMN_NOTE, description)
        editedNote.put(DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE, title)
        // Изменение записи
        database!!.update(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            editedNote,
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE + "= '" + title + "' AND " + DatabaseHelperForHistory.Companion.COLUMN_DATE + "= '" + date + "'",
            null
        )
    }

    // Удалить запись
    fun deleteNote(note: HistoryNote?) {
        val id = note!!.id
        database!!.delete(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            DatabaseHelperForHistory.Companion.COLUMN_ID + "=" + id,
            null
        )
    }

    fun deleteHistoryForCity(city: String?) {
        database!!.delete(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE + "= ?",
            arrayOf(city)
        )
    }

    // Очистить таблицу
    fun deleteAll() {
        database!!.delete(DatabaseHelperForHistory.Companion.TABLE_NOTES, null, null)
    }

    init {
        dbHelper = DatabaseHelperForHistory(context)
    }
}