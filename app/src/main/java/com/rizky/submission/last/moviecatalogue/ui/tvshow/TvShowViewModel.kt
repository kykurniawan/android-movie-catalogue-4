package com.rizky.submission.last.moviecatalogue.ui.tvshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class TvShowViewModel : ViewModel() {
    companion object {
        private const val API_KEY = "ad17726d6285f718d265aa2de12213bb"
    }

    val listTvShow = MutableLiveData<ArrayList<TvShowItems>>()

    internal fun getTvShow(): LiveData<ArrayList<TvShowItems>> {
        return listTvShow
    }

    internal fun setTvShow() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<TvShowItems>()
        val url = "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=en-US"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val tvShow = list.getJSONObject(i)
                        val tvShowItems = TvShowItems()

                        tvShowItems.id = tvShow.getInt("id")
                        tvShowItems.name = tvShow.getString("name")
                        tvShowItems.overview = tvShow.getString("overview")
                        tvShowItems.posterPath = tvShow.getString("poster_path")
                        tvShowItems.backdropPath = tvShow.getString("backdrop_path")
                        tvShowItems.firstAirDate = tvShow.getString("first_air_date")
                        tvShowItems.popularity = tvShow.getInt("popularity")
                        tvShowItems.voteAverage = tvShow.getInt("vote_average")
                        tvShowItems.voteCount = tvShow.getInt("vote_count")
                        listItems.add(tvShowItems)
                    }
                    listTvShow.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
                listTvShow.postValue(ArrayList())

            }
        })
    }

    internal fun setEmptyTvShow(){
        listTvShow.postValue(ArrayList())
    }

    internal fun searchTvShow(query: String?) {
        val client = AsyncHttpClient()
        val listItems = ArrayList<TvShowItems>()
        val url = "https://api.themoviedb.org/3/search/tv?api_key=${API_KEY}&language=en-US&query=$query"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val tvShow = list.getJSONObject(i)
                        val tvShowItems = TvShowItems()

                        tvShowItems.id = tvShow.getInt("id")
                        tvShowItems.name = tvShow.getString("name")
                        tvShowItems.overview = tvShow.getString("overview")
                        tvShowItems.posterPath = tvShow.getString("poster_path")
                        tvShowItems.backdropPath = tvShow.getString("backdrop_path")
                        tvShowItems.firstAirDate = tvShow.getString("first_air_date")
                        tvShowItems.popularity = tvShow.getInt("popularity")
                        tvShowItems.voteAverage = tvShow.getInt("vote_average")
                        tvShowItems.voteCount = tvShow.getInt("vote_count")
                        listItems.add(tvShowItems)
                    }
                    listTvShow.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
                listTvShow.postValue(ArrayList())

            }
        })
    }
}
