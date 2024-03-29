package com.casperkhv.weather

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TABLE_NOTES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_WEATHER_NOTE
                + " TEXT," + COLUMN_NOTE_TITLE_CITY + " TEXT);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion == 2) {
            val upgradeQuery =
                "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_NOTE_TITLE_CITY + " TEXT DEFAULT 'Title'"
            db.execSQL(upgradeQuery)
        }
    }

    companion object {
        private const val DATABASE_NAME = "cities.db"
        const val DATABASE_VERSION = 2
        const val TABLE_NOTES = "cities"
        const val COLUMN_ID = "_id"
        const val COLUMN_NOTE_TITLE_CITY = "title"
        const val COLUMN_WEATHER_NOTE = "note"
    }
}