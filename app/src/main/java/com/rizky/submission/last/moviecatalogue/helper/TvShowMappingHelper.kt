package com.rizky.submission.last.moviecatalogue.helper

import android.database.Cursor
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract
import com.rizky.submission.last.moviecatalogue.ui.tvshow.TvShowItems

object TvShowMappingHelper {

    fun mapCursorToArrayList(tvShowFavoriteCursor: Cursor): ArrayList<TvShowItems> {
        val tvShowFavoriteList = ArrayList<TvShowItems>()

        while (tvShowFavoriteCursor.moveToNext()) {
            val id = tvShowFavoriteCursor.getInt(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns._ID)
            )
            val name = tvShowFavoriteCursor.getString(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.NAME)
            )
            val firstAirDate = tvShowFavoriteCursor.getString(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.FIRST_AIR_DATE)
            )
            val overview = tvShowFavoriteCursor.getString(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.OVERVIEW)
            )
            val posterPath = tvShowFavoriteCursor.getString(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.POSTER_PATH)
            )
            val backdropPath = tvShowFavoriteCursor.getString(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.BACKDROP_PATH)
            )
            val voteCount = tvShowFavoriteCursor.getInt(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.VOTE_COUNT)
            )
            val popularity = tvShowFavoriteCursor.getInt(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.POPULARITY)
            )
            val voteAverage = tvShowFavoriteCursor.getInt(
                tvShowFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.VOTE_AVERAGE)
            )
            tvShowFavoriteList.add(
                TvShowItems(
                    id,
                    name,
                    firstAirDate,
                    overview,
                    posterPath,
                    voteCount,
                    popularity,
                    backdropPath,
                    voteAverage
                )
            )
        }
        return tvShowFavoriteList
    }
}