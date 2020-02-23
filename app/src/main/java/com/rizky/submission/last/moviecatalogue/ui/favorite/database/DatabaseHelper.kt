package com.rizky.submission.last.moviecatalogue.ui.favorite.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.MovieFavoriteColumns.Companion.TABLE_NAME_MOVIE
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.TvShowFavoriteColumns.Companion.TABLE_NAME_TVSHOW

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {

        private const val DATABASE_NAME = "mydb"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE $TABLE_NAME_MOVIE" +
                "(${DatabaseContract.MovieFavoriteColumns._ID} INTEGER UNIQUE NOT NULL," +
                "${DatabaseContract.MovieFavoriteColumns.TITLE} TEXT NOT NULL," +
                "${DatabaseContract.MovieFavoriteColumns.RELEASE_DATE} TEXT NOT NULL," +
                "${DatabaseContract.MovieFavoriteColumns.OVERVIEW} TEXT NOT NULL," +
                "${DatabaseContract.MovieFavoriteColumns.POSTER_PATH} TEXT NOT NULL," +
                "${DatabaseContract.MovieFavoriteColumns.VOTE_COUNT} INTEGER," +
                "${DatabaseContract.MovieFavoriteColumns.POPULARITY} INTEGER," +
                "${DatabaseContract.MovieFavoriteColumns.BACKDROP_PATH} TEXT NOT NULL," +
                "${DatabaseContract.MovieFavoriteColumns.VOTE_AVERAGE} INTEGER)"

        private const val SQL_CREATE_TABLE_TVSHOW = "CREATE TABLE $TABLE_NAME_TVSHOW" +
                "(${DatabaseContract.TvShowFavoriteColumns._ID} INTEGER UNIQUE NOT NULL," +
                "${DatabaseContract.TvShowFavoriteColumns.NAME} TEXT NOT NULL," +
                "${DatabaseContract.TvShowFavoriteColumns.FIRST_AIR_DATE} TEXT NOT NULL," +
                "${DatabaseContract.TvShowFavoriteColumns.OVERVIEW} TEXT NOT NULL," +
                "${DatabaseContract.TvShowFavoriteColumns.POSTER_PATH} TEXT NOT NULL," +
                "${DatabaseContract.TvShowFavoriteColumns.BACKDROP_PATH} TEXT NOT NULL," +
                "${DatabaseContract.TvShowFavoriteColumns.VOTE_COUNT} INTEGER," +
                "${DatabaseContract.TvShowFavoriteColumns.POPULARITY} INTEGER," +
                "${DatabaseContract.TvShowFavoriteColumns.VOTE_AVERAGE} INTEGER)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_TVSHOW)
        db.execSQL(SQL_CREATE_TABLE_MOVIE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_TVSHOW")
        onCreate(db)
    }
}