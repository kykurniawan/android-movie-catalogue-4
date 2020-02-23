package com.rizky.submission.last.moviecatalogue.ui.favorite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.MovieFavoriteColumns.Companion._ID
import java.sql.SQLException

class MovieFavoriteHelper(context: Context) {
    private val dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.MovieFavoriteColumns.TABLE_NAME_MOVIE
        private var INSTANCE: MovieFavoriteHelper? = null

        fun getInstance(context: Context): MovieFavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MovieFavoriteHelper(context)
                    }
                }
            }
            return INSTANCE as MovieFavoriteHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        this.database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )
    }

    fun queryById(id: String): Cursor {
        return this.database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return this.database.insertWithOnConflict(
            DATABASE_TABLE,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

}