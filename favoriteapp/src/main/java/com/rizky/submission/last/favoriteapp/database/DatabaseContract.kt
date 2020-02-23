package com.rizky.submission.last.favoriteapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.rizky.submission.last.moviecatalogue"
    const val AUTHORITY_2 = "com.rizky.submission.last.moviecatalogue.2"
    const val SCHEME = "content"

    internal class MovieFavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME_MOVIE = "moviefavorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val RELEASE_DATE = "release_date"
            const val OVERVIEW = "overview"
            const val POSTER_PATH = "poster_path"
            const val VOTE_COUNT = "vote_count"
            const val POPULARITY = "popularity"
            const val BACKDROP_PATH = "backdrop_path"
            const val VOTE_AVERAGE = "vote_average"

            val CONTENT_URI_MOVIE: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME_MOVIE)
                .build()
        }
    }

    internal class TvShowFavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME_TVSHOW = "tvshowfavorite"
            const val _ID = "_id"
            const val NAME = "name"
            const val FIRST_AIR_DATE = "first_air_date"
            const val OVERVIEW = "overview"
            const val POSTER_PATH = "poster_path"
            const val BACKDROP_PATH = "backdrop_path"
            const val VOTE_COUNT = "vote_count"
            const val POPULARITY = "popularity"
            const val VOTE_AVERAGE = "vote_average"

            val CONTENT_URI_TV_SHOW: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY_2)
                .appendPath(TABLE_NAME_TVSHOW)
                .build()
        }
    }
}