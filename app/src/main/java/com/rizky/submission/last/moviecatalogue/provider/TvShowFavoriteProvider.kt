package com.rizky.submission.last.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.AUTHORITY_2
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.TvShowFavoriteColumns.Companion.CONTENT_URI_TV_SHOW
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract.TvShowFavoriteColumns.Companion.TABLE_NAME_TVSHOW
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.TvShowFavoriteHelper

class TvShowFavoriteProvider : ContentProvider() {

    companion object {
        private const val TV_SHOW_FAVORITE = 1
        private const val TV_SHOW_FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var tvShowFavoriteHelper: TvShowFavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY_2, TABLE_NAME_TVSHOW, TV_SHOW_FAVORITE)

            sUriMatcher.addURI(AUTHORITY_2, "$TABLE_NAME_TVSHOW/#", TV_SHOW_FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(context as Context)
        tvShowFavoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(context as Context)
        tvShowFavoriteHelper.open()
        return when (sUriMatcher.match(uri)) {
            TV_SHOW_FAVORITE -> tvShowFavoriteHelper.queryAll()
            TV_SHOW_FAVORITE_ID -> tvShowFavoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (TV_SHOW_FAVORITE) {
            sUriMatcher.match(uri) -> tvShowFavoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI_TV_SHOW, null)
        return Uri.parse("$CONTENT_URI_TV_SHOW/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (TV_SHOW_FAVORITE_ID) {
            sUriMatcher.match(uri) -> tvShowFavoriteHelper.update(
                uri.lastPathSegment.toString(),
                values
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI_TV_SHOW, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (TV_SHOW_FAVORITE_ID) {
            sUriMatcher.match(uri) -> tvShowFavoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI_TV_SHOW, null)
        return deleted
    }
}
