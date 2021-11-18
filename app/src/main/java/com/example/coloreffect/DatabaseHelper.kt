package com.example.coloreffect

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Вызывается при попытке доступа к базе данных, когда она еще не создана
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TABLE_NOTES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NOTE
                + " TEXT," + COLUMN_NOTE_TITLE + " TEXT);"
        )
    }

    // Вызывается, когда необходимо обновление базы данных
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion == 2) {
            val upgradeQuery =
                "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_NOTE_TITLE + " TEXT DEFAULT 'Title'"
            db.execSQL(upgradeQuery)
        }
    }

    companion object {

        private const val DATABASE_NAME = "cities.db" // Название БД
        const val DATABASE_VERSION = 2 // Версия базы данных
        const val TABLE_NOTES = "cities" // Название таблицы в БД

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_NOTE_TITLE = "title"
        const val COLUMN_NOTE = "note"
    }
}