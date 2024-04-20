package com.casperkhv.weather

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.io.Closeable
import java.io.IOException

class NoteDataReaderForHistory(
    private val database: SQLiteDatabase?
) : Closeable {
    private var cursor: Cursor? = null
    private val notesAllColumn = arrayOf<String>(
        DatabaseHelperForHistory.Companion.COLUMN_ID,
        DatabaseHelperForHistory.Companion.COLUMN_DATE,
        DatabaseHelperForHistory.Companion.COLUMN_WEATHER_NOTE,
        DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE_CITY
    )

    fun open(city: String?) {
        query(city)
        cursor!!.moveToFirst()
    }

    @Throws(IOException::class)
    override fun close() {
        cursor!!.close()
    }

    fun refresh(city: String?) {
        val position = cursor!!.position
        query(city)
        cursor!!.moveToPosition(position)
    }

    private fun query(city: String?) {
        cursor = database!!.query(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            notesAllColumn,
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE_CITY + "= ?",
            arrayOf(city),
            null,
            null,
            null
        )
    }

    fun getCountForAvoidRepetition(city: String, date: String): Int {
        return database!!.query(
            DatabaseHelperForHistory.Companion.TABLE_NOTES,
            notesAllColumn,
            DatabaseHelperForHistory.Companion.COLUMN_NOTE_TITLE_CITY + "= ? AND " +
                DatabaseHelperForHistory.Companion.COLUMN_DATE + "= ?",
            arrayOf(city, date),
            null,
            null,
            null
        ).count
    }

    fun getPosition(position: Int): HistoryNote {
        cursor!!.moveToPosition(position)
        return cursorToNote()
    }

    val count: Int
        get() = cursor!!.count

    private fun cursorToNote(): HistoryNote {
        val note = HistoryNote()
        note.id = cursor!!.getLong(0)
        note.date = cursor!!.getString(1)
        note.descriptionWeather = cursor!!.getString(2)
        note.titleCity = cursor!!.getString(3)
        return note
    }
}