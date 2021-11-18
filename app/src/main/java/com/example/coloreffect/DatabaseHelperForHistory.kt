package com.example.coloreffect

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelperForHistory(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TABLE_NOTES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE + " TEXT," + COLUMN_NOTE
                + " TEXT," + COLUMN_NOTE_TITLE + " TEXT);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion == 2) {
            val upgradeQuery =
                "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_NOTE_TITLE + " TEXT DEFAULT 'Title'"
            db.execSQL(upgradeQuery)
        }
    }

    companion object {

        private const val DATABASE_NAME = "history.db" // Название БД
        const val DATABASE_VERSION = 2 // Версия базы данных
        const val TABLE_NOTES = "history" // Название таблицы в БД

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date" // день сбора данных
        const val COLUMN_NOTE_TITLE = "title" // Здесь должен быть город
        const val COLUMN_NOTE = "note" // Здесь должны быть сохранённые данные о погоде
    }
}