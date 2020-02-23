package com.rizky.submission.last.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.AUTHORITY
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.MovieFavoriteColumns.Companion.CONTENT_URI_MOVIE
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.MovieFavoriteColumns.Companion.TABLE_NAME_MOVIE
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.MovieFavoriteHelper

class MovieFavoriteProvider : ContentProvider() {

    companion object {
        private const val MOVIE_FAVORITE = 1
        private const val MOVIE_FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieFavoriteHelper: MovieFavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME_MOVIE, MOVIE_FAVORITE)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME_MOVIE/#", MOVIE_FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(context as Context)
        movieFavoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(context as Context)
        movieFavoriteHelper.open()
        return when (sUriMatcher.match(uri)) {
            MOVIE_FAVORITE -> movieFavoriteHelper.queryAll()
            MOVIE_FAVORITE_ID -> movieFavoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (MOVIE_FAVORITE) {
            sUriMatcher.match(uri) -> movieFavoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
        return Uri.parse("$CONTENT_URI_MOVIE/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (MOVIE_FAVORITE_ID) {
            sUriMatcher.match(uri) -> movieFavoriteHelper.update(
                uri.lastPathSegment.toString(),
                values
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (MOVIE_FAVORITE_ID) {
            sUriMatcher.match(uri) -> movieFavoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
        return deleted
    }
}
