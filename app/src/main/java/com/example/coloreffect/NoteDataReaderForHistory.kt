package com.example.coloreffect

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.coloreffect.DatabaseHelperForHistory
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
        DatabaseHelperForHistory.COLUMN_ID,
        DatabaseHelperForHistory.COLUMN_DATE,
        DatabaseHelperForHistory.COLUMN_NOTE,
        DatabaseHelperForHistory.COLUMN_NOTE_TITLE
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
            DatabaseHelperForHistory.TABLE_NOTES,
            notesAllColumn,
            DatabaseHelperForHistory.COLUMN_NOTE_TITLE + "= ?",
            arrayOf(city),
            null,
            null,
            null
        )
    }

    // Создание запроса на курсор для определения, имеются ли данные по городу на определённую дату
    fun getCountForAvoidRepetition(city: String, date: String): Int {
        return database!!.query(
            DatabaseHelperForHistory.TABLE_NOTES,
            notesAllColumn,
            DatabaseHelperForHistory.COLUMN_NOTE_TITLE + "= ? AND " + DatabaseHelperForHistory.COLUMN_DATE + "= ?",
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