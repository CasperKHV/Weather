package com.example.coloreffect;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

//  Источник данных, позволяет изменять данные в таблице
// Создает и держит в себе читатель данных
public class NoteDataSource implements Closeable {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private NoteDataReader noteDataReader;

    public NoteDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Открывает базу данных
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        // Создать читателя и открыть его
        noteDataReader = new NoteDataReader(database);
        noteDataReader.open();
    }

    @Override
    public void close() throws IOException {
        noteDataReader.close();
        dbHelper.close();
    }

    // Добавить новую запись
    public CityNote addNote(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE,description);
        values.put(DatabaseHelper.COLUMN_NOTE_TITLE,title);
        // Добавление записи
        long insertId = database.insert(DatabaseHelper.TABLE_NOTES,null,values);
        CityNote note = new CityNote();
        note.setId(insertId);
        note.setTitle(title);
        note.setDescription(description);
        return note;
    }

    // Изменить запись
    public void editNote(CityNote note, String title, String description) {
        ContentValues editedNote = new ContentValues();
        editedNote.put(DatabaseHelper.COLUMN_ID, note.getId());
        editedNote.put(DatabaseHelper.COLUMN_NOTE, description);
        editedNote.put(DatabaseHelper.COLUMN_NOTE_TITLE,title);
        // Изменение записи
        database.update(DatabaseHelper.TABLE_NOTES,
                editedNote,
                DatabaseHelper.COLUMN_ID + "=" + note.getId(),
                null);
    }

    // Удалить запись
    public void deleteNote(CityNote note) {
        long id = note.getId();
        database.delete(DatabaseHelper.TABLE_NOTES,
                DatabaseHelper.COLUMN_ID + "=" + id,
                null);
    }

    // Очистить таблицу
    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_NOTES, null, null);
    }

    // Вернуть читателя (он потребуется в других местах)
    public NoteDataReader getNoteDataReader(){
        return noteDataReader;
    }

}
