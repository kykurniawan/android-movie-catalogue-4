package com.rizky.submission.last.moviecatalogue.ui.favorite.movie

import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizky.submission.last.moviecatalogue.helper.MovieMappingHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.MovieFavoriteHelper
import com.rizky.submission.last.moviecatalogue.ui.movie.MovieItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieFavoriteViewModel : ViewModel() {
    private val listMovie = MutableLiveData<ArrayList<MovieItems>>()
    internal fun getMovieFavorite(): LiveData<ArrayList<MovieItems>> {
        return listMovie
    }

    internal fun setMovieFavorite(helper: MovieFavoriteHelper) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovieFavorite = async(Dispatchers.IO) {
                val cursor = helper.queryAll()
                MovieMappingHelper.mapCursorToArrayList(cursor)
            }
            val movieFavorite = deferredMovieFavorite.await()
            if (movieFavorite.size > 0) {
                listMovie.postValue(movieFavorite)
            } else {
                listMovie.postValue(ArrayList())
            }
        }
    }

    internal fun saveMovieToFavorite(movie: MovieItems, helper: MovieFavoriteHelper): Long {
        val values = ContentValues()
        values.put(DatabaseContract.MovieFavoriteColumns._ID, movie.id)
        values.put(DatabaseContract.MovieFavoriteColumns.TITLE, movie.title)
        values.put(DatabaseContract.MovieFavoriteColumns.RELEASE_DATE, movie.releaseDate)
        values.put(DatabaseContract.MovieFavoriteColumns.OVERVIEW, movie.overview)
        values.put(DatabaseContract.MovieFavoriteColumns.POSTER_PATH, movie.posterPath)
        values.put(DatabaseContract.MovieFavoriteColumns.VOTE_COUNT, movie.voteCount)
        values.put(DatabaseContract.MovieFavoriteColumns.POPULARITY, movie.popularity)
        values.put(DatabaseContract.MovieFavoriteColumns.BACKDROP_PATH, movie.backdropPath)
        values.put(DatabaseContract.MovieFavoriteColumns.VOTE_AVERAGE, movie.voteAverage)

        return helper.insert(values)
    }

    internal fun deleteMovieFromFavorite(movie: MovieItems, helper: MovieFavoriteHelper): Int {
        return helper.deleteById(movie.id.toString())
    }
}
