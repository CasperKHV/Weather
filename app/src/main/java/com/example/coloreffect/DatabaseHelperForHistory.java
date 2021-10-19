package com.example.coloreffect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperForHistory extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "history.db"; // Название БД
    public static final int DATABASE_VERSION = 2; // Версия базы данных
    static final String TABLE_NOTES = "history"; // Название таблицы в БД
    // Названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";// день сбора данных
    public static final String COLUMN_NOTE_TITLE = "title";// Здесь должен быть город
    public static final String COLUMN_NOTE = "note";// Здесь должны быть сохранённые данные о погоде

    public DatabaseHelperForHistory(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE + " TEXT," + COLUMN_NOTE
                + " TEXT," + COLUMN_NOTE_TITLE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ((oldVersion == 1) && (newVersion == 2)) {
            String upgradeQuery = "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_NOTE_TITLE + " TEXT DEFAULT 'Title'";
            db.execSQL(upgradeQuery);
        }
    }
}
