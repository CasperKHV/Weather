package com.example.coloreffect;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

// Читатель источника данных на основе курсора
// Этот класс был вынесен из NoteDataSource, чтобы разгрузить его ответственности
public class NoteDataReader implements Closeable {

    private Cursor cursor;              // Курсор (фактически, подготовленный запрос),
    // но сами данные подсчитываются только по необходимости
    private SQLiteDatabase database;

    private String[] notesAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NOTE,
            DatabaseHelper.COLUMN_NOTE_TITLE
    };

    public NoteDataReader(SQLiteDatabase database) {
        this.database = database;
    }

    // Подготовить к чтению таблицу
    public void open() {
        query();
        cursor.moveToFirst();
    }

    @Override
    public void close() throws IOException {
        cursor.close();
    }

    // Перечитать таблицу (если точно – обновить курсор)
    public void Refresh() {
        int position = cursor.getPosition();
        query();
        cursor.moveToPosition(position);
    }


    // Создание запроса на курсор
    private void query() {
        cursor = database.query(DatabaseHelper.TABLE_NOTES,
                notesAllColumn, null, null, null, null, null);
    }

    // Прочитать данные по определенной позиции
    public CityNote getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    // Получить количество строк в таблице
    public int getCount() {
        return cursor.getCount();
    }

    // Преобразователь данных курсора в объект
    private CityNote cursorToNote() {
        CityNote note = new CityNote();
        note.setId(cursor.getLong(0));
        note.setDescription(cursor.getString(1));
        note.setTitle(cursor.getString(2));
        return note;
    }
}
