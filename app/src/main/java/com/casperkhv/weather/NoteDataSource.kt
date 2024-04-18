package com.casperkhv.weather

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.io.Closeable
import java.io.IOException

class NoteDataSource(context: Context?) : Closeable {
    private val dbHelper: DatabaseHelper
    private var database: SQLiteDatabase? = null

    var noteDataReader: NoteDataReader? = null
        private set

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
        noteDataReader = NoteDataReader(database)
        noteDataReader!!.open()
    }

    @Throws(IOException::class)
    override fun close() {
        noteDataReader!!.close()
        dbHelper.close()
    }

    fun addNote(title: String?, description: String?): CityNote {
        val values = ContentValues()
        values.put(DatabaseHelper.Companion.COLUMN_WEATHER_NOTE, description)
        values.put(DatabaseHelper.Companion.COLUMN_NOTE_TITLE_CITY, title)
        val insertId = database!!.insert(DatabaseHelper.Companion.TABLE_NOTES, null, values)
        val note = CityNote()
        note.id = insertId
        note.title = title
        note.description = description
        return note
    }

    fun editNote(note: CityNote?, title: String?, description: String?) {
        val editedNote = ContentValues()
        editedNote.put(DatabaseHelper.Companion.COLUMN_ID, note!!.id)
        editedNote.put(DatabaseHelper.Companion.COLUMN_WEATHER_NOTE, description)
        editedNote.put(DatabaseHelper.Companion.COLUMN_NOTE_TITLE_CITY, title)
        database!!.update(
            DatabaseHelper.Companion.TABLE_NOTES,
            editedNote,
            DatabaseHelper.Companion.COLUMN_ID + "=" + note.id,
            null
        )
    }

    fun deleteNote(note: CityNote?) {
        val id = note!!.id
        database!!.delete(
            DatabaseHelper.Companion.TABLE_NOTES,
            DatabaseHelper.Companion.COLUMN_ID + "=" + id,
            null
        )
    }

    fun deleteAll() {
        database!!.delete(DatabaseHelper.Companion.TABLE_NOTES, null, null)
    }

    init {
        dbHelper = DatabaseHelper(context)
    }
}