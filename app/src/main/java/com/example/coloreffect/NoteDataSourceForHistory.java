package com.example.coloreffect;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

//  Источник данных, позволяет изменять данные в таблице
// Создает и держит в себе читатель данных
public class NoteDataSourceForHistory implements Closeable {
    private DatabaseHelperForHistory dbHelper;
    private SQLiteDatabase database;
    private NoteDataReaderForHistory noteDataReaderForHistory;

    public NoteDataSourceForHistory(Context context) {
        dbHelper = new DatabaseHelperForHistory(context);
    }

    // Открывает базу данных
    public void open(String city) throws SQLException {
        database = dbHelper.getWritableDatabase();
        // Создать читателя и открыть его
        noteDataReaderForHistory = new NoteDataReaderForHistory(database);
        noteDataReaderForHistory.open(city);
    }

    @Override
    public void close() throws IOException {
        noteDataReaderForHistory.close();
        dbHelper.close();
    }

    // Добавить новую запись
    public HistoryNote addNote(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperForHistory.COLUMN_NOTE,description);
        values.put(DatabaseHelperForHistory.COLUMN_NOTE_TITLE,title);
        // Добавление записи
        long insertId = database.insert(DatabaseHelperForHistory.TABLE_NOTES,null,values);
        HistoryNote note = new HistoryNote();
        note.setId(insertId);
        note.setTitle(title);
        note.setDescription(description);
        return note;
    }

    // Изменить запись
    public void editNote(String title, String description) {
        ContentValues editedNote = new ContentValues();
        editedNote.put(DatabaseHelperForHistory.COLUMN_NOTE, description);
        editedNote.put(DatabaseHelperForHistory.COLUMN_NOTE_TITLE,title);
        // Изменение записи
        database.update(DatabaseHelperForHistory.TABLE_NOTES,
                editedNote,
                DatabaseHelperForHistory.COLUMN_NOTE_TITLE + "= '" + title + "'",
                null);
    }

    // Удалить запись
    public void deleteNote(HistoryNote note) {
        long id = note.getId();
        database.delete(DatabaseHelperForHistory.TABLE_NOTES,
                DatabaseHelperForHistory.COLUMN_ID + "=" + id,
                null);
    }

    // Очистить таблицу
    public void deleteAll() {
        database.delete(DatabaseHelperForHistory.TABLE_NOTES, null, null);
    }

    // Вернуть читателя (он потребуется в других местах)
    public NoteDataReaderForHistory getNoteDataReaderForHistory(){
        return noteDataReaderForHistory;
    }
}
