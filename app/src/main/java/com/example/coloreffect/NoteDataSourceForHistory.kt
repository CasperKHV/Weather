package com.example.coloreffect

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.coloreffect.DatabaseHelperForHistory
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
        values.put(DatabaseHelperForHistory.COLUMN_DATE, date)
        values.put(DatabaseHelperForHistory.COLUMN_NOTE, description)
        values.put(DatabaseHelperForHistory.COLUMN_NOTE_TITLE, title)
        // Добавление записи
        val insertId = database!!.insert(DatabaseHelperForHistory.TABLE_NOTES, null, values)
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
        editedNote.put(DatabaseHelperForHistory.COLUMN_DATE, date)
        editedNote.put(DatabaseHelperForHistory.COLUMN_NOTE, description)
        editedNote.put(DatabaseHelperForHistory.COLUMN_NOTE_TITLE, title)
        // Изменение записи
        database!!.update(
            DatabaseHelperForHistory.TABLE_NOTES,
            editedNote,
            DatabaseHelperForHistory.COLUMN_NOTE_TITLE + "= '" + title + "' AND " + DatabaseHelperForHistory.COLUMN_DATE + "= '" + date + "'",
            null
        )
    }

    // Удалить запись
    fun deleteNote(note: HistoryNote?) {
        val id = note!!.id
        database!!.delete(
            DatabaseHelperForHistory.TABLE_NOTES,
            DatabaseHelperForHistory.COLUMN_ID + "=" + id,
            null
        )
    }

    fun deleteHistoryForCity(city: String?) {
        database!!.delete(
            DatabaseHelperForHistory.TABLE_NOTES,
            DatabaseHelperForHistory.COLUMN_NOTE_TITLE + "= ?",
            arrayOf(city)
        )
    }

    // Очистить таблицу
    fun deleteAll() {
        database!!.delete(DatabaseHelperForHistory.TABLE_NOTES, null, null)
    }

    init {
        dbHelper = DatabaseHelperForHistory(context)
    }
}