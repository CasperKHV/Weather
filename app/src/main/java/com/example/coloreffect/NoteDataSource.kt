package com.example.coloreffect

//import com.example.coloreffect.Controller.start
import android.content.*
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.io.Closeable
import java.io.IOException

//  Источник данных, позволяет изменять данные в таблице
// Создает и держит в себе читатель данных
class NoteDataSource(context: Context?) : Closeable {
    private val dbHelper: DatabaseHelper
    private var database: SQLiteDatabase? = null

    // Вернуть читателя (он потребуется в других местах)
    var noteDataReader: NoteDataReader? = null
        private set

    // Открывает базу данных
    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
        // Создать читателя и открыть его
        noteDataReader = NoteDataReader(database)
        noteDataReader!!.open()
    }

    @Throws(IOException::class)
    override fun close() {
        noteDataReader!!.close()
        dbHelper.close()
    }

    // Добавить новую запись
    fun addNote(title: String?, description: String?): CityNote {
        val values = ContentValues()
        values.put(DatabaseHelper.Companion.COLUMN_NOTE, description)
        values.put(DatabaseHelper.Companion.COLUMN_NOTE_TITLE, title)
        // Добавление записи
        val insertId = database!!.insert(DatabaseHelper.Companion.TABLE_NOTES, null, values)
        val note = CityNote()
        note.id = insertId
        note.title = title
        note.description = description
        return note
    }

    // Изменить запись
    fun editNote(note: CityNote?, title: String?, description: String?) {
        val editedNote = ContentValues()
        editedNote.put(DatabaseHelper.Companion.COLUMN_ID, note!!.id)
        editedNote.put(DatabaseHelper.Companion.COLUMN_NOTE, description)
        editedNote.put(DatabaseHelper.Companion.COLUMN_NOTE_TITLE, title)
        // Изменение записи
        database!!.update(
            DatabaseHelper.Companion.TABLE_NOTES,
            editedNote,
            DatabaseHelper.Companion.COLUMN_ID + "=" + note.id,
            null
        )
    }

    // Удалить запись
    fun deleteNote(note: CityNote?) {
        val id = note!!.id
        database!!.delete(
            DatabaseHelper.Companion.TABLE_NOTES,
            DatabaseHelper.Companion.COLUMN_ID + "=" + id,
            null
        )
    }

    // Очистить таблицу
    fun deleteAll() {
        database!!.delete(DatabaseHelper.Companion.TABLE_NOTES, null, null)
    }

    init {
        dbHelper = DatabaseHelper(context)
    }
}