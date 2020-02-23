package com.rizky.submission.last.moviecatalogue.ui.favorite.tvshow

import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizky.submission.last.moviecatalogue.helper.TvShowMappingHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.DatabaseContract
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.TvShowFavoriteHelper
import com.rizky.submission.last.moviecatalogue.ui.tvshow.TvShowItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TvShowFavoriteViewModel : ViewModel() {
    private val listTvShow = MutableLiveData<ArrayList<TvShowItems>>()
    internal fun getTvShowFavorite(): LiveData<ArrayList<TvShowItems>>{
        return listTvShow
    }

    internal fun setTvShowFavorite(helper: TvShowFavoriteHelper){
        GlobalScope.launch(Dispatchers.Main){
            val deferredTvShowFavorite = async(Dispatchers.IO) {
                val cursor = helper.queryAll()
                TvShowMappingHelper.mapCursorToArrayList(cursor)
            }
            val tvShowFavorite = deferredTvShowFavorite.await()
            if (tvShowFavorite.size > 0) {
                listTvShow.postValue(tvShowFavorite)
            } else {
                listTvShow.postValue(ArrayList())
            }
        }
    }

    internal fun saveTvShowToFavorite(tvShow: TvShowItems, helper: TvShowFavoriteHelper): Long {
        val values = ContentValues()
        values.put(DatabaseContract.TvShowFavoriteColumns._ID, tvShow.id)
        values.put(DatabaseContract.TvShowFavoriteColumns.NAME, tvShow.name)
        values.put(DatabaseContract.TvShowFavoriteColumns.FIRST_AIR_DATE, tvShow.firstAirDate)
        values.put(DatabaseContract.TvShowFavoriteColumns.OVERVIEW, tvShow.overview)
        values.put(DatabaseContract.TvShowFavoriteColumns.POSTER_PATH, tvShow.posterPath)
        values.put(DatabaseContract.TvShowFavoriteColumns.BACKDROP_PATH, tvShow.backdropPath)
        values.put(DatabaseContract.TvShowFavoriteColumns.VOTE_COUNT, tvShow.voteCount)
        values.put(DatabaseContract.TvShowFavoriteColumns.POPULARITY, tvShow.popularity)
        values.put(DatabaseContract.TvShowFavoriteColumns.VOTE_AVERAGE, tvShow.voteAverage)

        return helper.insert(values)
    }

    internal fun deleteTvShowFromFavorite(tvShow: TvShowItems, helper: TvShowFavoriteHelper): Int {
        return helper.deleteById(tvShow.id.toString())
    }
}
