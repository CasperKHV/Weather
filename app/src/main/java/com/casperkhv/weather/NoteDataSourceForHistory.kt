package com.casperkhv.weather

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.io.Closeable
import java.io.IOException

class NoteDataSourceForHistory(context: Context?) : Closeable {
    private val dbHelper: DatabaseHelperForHistory
    private var database: SQLiteDatabase? = null

    var noteDataReaderForHistory: NoteDataReaderForHistory? = null
        private set

    @Throws(SQLException::class)
    fun open(city: String?) {
        database = dbHelper.writableDatabase
        noteDataReaderForHistory = NoteDataReaderForHistory(database)
        noteDataReaderForHistory!!.open(city)
    }

    @Throws(IOException::class)
    override fun close() {
        noteDataReaderForHistory!!.close()
        dbHelper.close()
    }

    fun addNote(date: String?, title: String?, description: String?): HistoryNote {
        val values = ContentValues()
        values.put(DatabaseHelperForHistory.Companion.COLUMN_DATE, date)
        values.put(DatabaseHelperForHistory.Companion.COLUMN_WEATHER_NOTE, description)
        values.put(DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE_CITY, title)
        val insertId =
            database!!.insert(DatabaseHelperForHistory.Companion.TABLE_NOTES, null, values)
        val note = HistoryNote()
        note.id = insertId
        note.date = date
        note.titleCity = title
        note.descriptionWeather = description
        return note
    }

    fun editNote(date: String, title: String, description: String?) {
        val editedNote = ContentValues()
        editedNote.put(DatabaseHelperForHistory.Companion.COLUMN_DATE, date)
        editedNote.put(DatabaseHelperForHistory.Companion.COLUMN_WEATHER_NOTE, description)
        editedNote.put(DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE_CITY, title)
        database!!.update(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            editedNote,
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE_CITY + "= '" + title + "' AND " +
                DatabaseHelperForHistory.Companion.COLUMN_DATE + "= '" + date + "'",
            null
        )
    }

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
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE_CITY + "= ?",
            arrayOf(city)
        )
    }

    fun deleteAll() {
        database!!.delete(DatabaseHelperForHistory.Companion.TABLE_NOTES, null, null)
    }

    init {
        dbHelper = DatabaseHelperForHistory(context)
    }
}