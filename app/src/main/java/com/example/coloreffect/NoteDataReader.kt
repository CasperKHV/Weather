package com.example.coloreffect

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.coloreffect.DatabaseHelper
import java.io.Closeable
import java.io.IOException

// Читатель источника данных на основе курсора
// Этот класс был вынесен из NoteDataSource, чтобы разгрузить его ответственности
class NoteDataReader(  // но сами данные подсчитываются только по необходимости
    private val database: SQLiteDatabase?
) : Closeable {

    private var cursor // Курсор (фактически, подготовленный запрос),
        : Cursor? = null
    private val notesAllColumn = arrayOf<String>(
        DatabaseHelper.COLUMN_ID,
        DatabaseHelper.COLUMN_NOTE,
        DatabaseHelper.COLUMN_NOTE_TITLE
    )

    // Подготовить к чтению таблицу
    fun open() {
        query()
        cursor!!.moveToFirst()
    }

    @Throws(IOException::class)
    override fun close() {
        cursor!!.close()
    }

    // Перечитать таблицу (если точно – обновить курсор)
    fun Refresh() {
        val position = cursor!!.position
        query()
        cursor!!.moveToPosition(position)
    }

    // Создание запроса на курсор
    private fun query() {
        cursor = database!!.query(
            DatabaseHelper.TABLE_NOTES,
            notesAllColumn, null, null, null, null, null
        )
    }

    // Прочитать данные по определенной позиции
    fun getPosition(position: Int): CityNote {
        cursor!!.moveToPosition(position)
        return cursorToNote()
    }

    // Получить количество строк в таблице
    val count: Int
        get() = cursor!!.count

    // Преобразователь данных курсора в объект
    private fun cursorToNote(): CityNote {
        val note = CityNote()
        note.id = cursor!!.getLong(0)
        note.description = cursor!!.getString(1)
        note.title = cursor!!.getString(2)
        return note
    }
}