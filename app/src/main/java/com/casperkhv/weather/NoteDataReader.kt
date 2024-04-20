package com.casperkhv.weather

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.io.Closeable
import java.io.IOException

class NoteDataReader(
    private val database: SQLiteDatabase?
) : Closeable {
    private var cursor: Cursor? = null
    private val notesAllColumn = arrayOf<String>(
        DatabaseHelper.Companion.COLUMN_ID,
        DatabaseHelper.Companion.COLUMN_WEATHER_NOTE,
        DatabaseHelper.Companion.COLUMN_NOTE_TITLE_CITY
    )

    fun open() {
        query()
        cursor!!.moveToFirst()
    }

    @Throws(IOException::class)
    override fun close() {
        cursor!!.close()
    }

    fun refresh() {
        val position = cursor!!.position
        query()
        cursor!!.moveToPosition(position)
    }

    private fun query() {
        cursor = database!!.query(
            DatabaseHelper.Companion.TABLE_NOTES,
            notesAllColumn, null, null, null, null, null
        )
    }

    fun getPosition(position: Int): CityNote {
        cursor!!.moveToPosition(position)
        return cursorToNote()
    }

    val count: Int
        get() = cursor!!.count

    private fun cursorToNote(): CityNote {
        val note = CityNote()
        note.id = cursor!!.getLong(0)
        note.description = cursor!!.getString(1)
        note.title = cursor!!.getString(2)
        return note
    }
}