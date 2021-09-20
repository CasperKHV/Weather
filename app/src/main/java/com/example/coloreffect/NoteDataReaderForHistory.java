package com.example.coloreffect;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

// Читатель источника данных на основе курсора
// Этот класс был вынесен из NoteDataSource, чтобы разгрузить его ответственности
public class NoteDataReaderForHistory implements Closeable {

    private Cursor cursor;              // Курсор (фактически, подготовленный запрос),
    // но сами данные подсчитываются только по необходимости
    private SQLiteDatabase database;

    private String[] notesAllColumn = {
            DatabaseHelperForHistory.COLUMN_ID,
            DatabaseHelperForHistory.COLUMN_NOTE,
            DatabaseHelperForHistory.COLUMN_NOTE_TITLE
    };

    public NoteDataReaderForHistory(SQLiteDatabase database) {
        this.database = database;
    }

    // Подготовить к чтению таблицу
    public void open(String city) {
        query(city);
        cursor.moveToFirst();
    }

    @Override
    public void close() throws IOException {
        cursor.close();
    }

    // Перечитать таблицу (если точно – обновить курсор)
    public void Refresh(String city) {
        int position = cursor.getPosition();
        query(city);
        cursor.moveToPosition(position);
    }


    // Создание запроса на курсор
    private void query(String city) {
        cursor = database.query(DatabaseHelperForHistory.TABLE_NOTES,
                notesAllColumn, DatabaseHelperForHistory.COLUMN_NOTE_TITLE + "= ?", new String[] {city}, null, null, null);

    }

    // Прочитать данные по определенной позиции
    public HistoryNote getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    // Получить количество строк в таблице
    public int getCount() {
        return cursor.getCount();
    }

    // Преобразователь данных курсора в объект
    private HistoryNote cursorToNote() {
        HistoryNote note = new HistoryNote();
        note.setId(cursor.getLong(0));
        note.setDescription(cursor.getString(1));
        note.setTitle(cursor.getString(2));
        return note;
    }
}
