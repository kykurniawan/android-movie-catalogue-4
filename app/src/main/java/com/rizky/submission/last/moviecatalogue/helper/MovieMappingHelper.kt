package com.rizky.submission.last.moviecatalogue.helper

import android.database.Cursor
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract
import com.rizky.submission.last.moviecatalogue.ui.movie.MovieItems

object MovieMappingHelper {

    fun mapCursorToArrayList(movieFavoriteCursor: Cursor): ArrayList<MovieItems> {
        val movieFavoriteList = ArrayList<MovieItems>()
        while (movieFavoriteCursor.moveToNext()) {
            val id = movieFavoriteCursor.getInt(
                movieFavoriteCursor.getColumnIndexOrThrow(
                    DatabaseContract.MovieFavoriteColumns._ID
                )
            )
            val title = movieFavoriteCursor.getString(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.TITLE)
            )
            val releaseDate = movieFavoriteCursor.getString(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.RELEASE_DATE)
            )
            val overview = movieFavoriteCursor.getString(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.OVERVIEW)
            )
            val posterPath = movieFavoriteCursor.getString(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.POSTER_PATH)
            )
            val voteCount = movieFavoriteCursor.getInt(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.VOTE_COUNT)
            )
            val popularity = movieFavoriteCursor.getInt(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.POPULARITY)
            )
            val backdropPath = movieFavoriteCursor.getString(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.BACKDROP_PATH)
            )
            val voteAverage = movieFavoriteCursor.getInt(
                movieFavoriteCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.VOTE_AVERAGE)
            )
            movieFavoriteList.add(
                MovieItems(
                    id,
                    title,
                    releaseDate,
                    overview,
                    posterPath,
                    voteCount,
                    popularity,
                    backdropPath,
                    voteAverage
                )
            )
        }
        return movieFavoriteList
    }
}