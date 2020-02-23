package com.rizky.submission.last.moviecatalogue.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MovieViewModel: ViewModel() {
    companion object {
        private const val API_KEY = "ad17726d6285f718d265aa2de12213bb"
    }

    val listMovie = MutableLiveData<ArrayList<MovieItems>>()

    internal fun getMovie(): LiveData<ArrayList<MovieItems>> {
        return listMovie
    }

    internal fun setMovie() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<MovieItems>()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&language=en-US"
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
                        val movie = list.getJSONObject(i)
                        val movieItems = MovieItems()

                        movieItems.id = movie.getInt("id")
                        movieItems.title = movie.getString("title")
                        movieItems.overview = movie.getString("overview")
                        movieItems.posterPath = movie.getString("poster_path")
                        movieItems.backdropPath = movie.getString("backdrop_path")
                        movieItems.releaseDate = movie.getString("release_date")
                        movieItems.popularity = movie.getInt("popularity")
                        movieItems.voteCount = movie.getInt("vote_count")
                        movieItems.voteAverage = movie.getInt("vote_average")
                        listItems.add(movieItems)
                    }
                    listMovie.postValue(listItems)
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
                listMovie.postValue(ArrayList())

            }
        })
    }

    internal fun setEmptyMovie(){
        listMovie.postValue(ArrayList())
    }

    internal fun searchMovie(query: String?){
        val client = AsyncHttpClient()
        val listItems = ArrayList<MovieItems>()
        val url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&query=$query"
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
                        val movie = list.getJSONObject(i)
                        val movieItems = MovieItems()

                        movieItems.id = movie.getInt("id")
                        movieItems.title = movie.getString("title")
                        movieItems.overview = movie.getString("overview")
                        movieItems.posterPath = movie.getString("poster_path")
                        movieItems.backdropPath = movie.getString("backdrop_path")
                        movieItems.releaseDate = movie.getString("release_date")
                        movieItems.popularity = movie.getInt("popularity")
                        movieItems.voteCount = movie.getInt("vote_count")
                        movieItems.voteAverage = movie.getInt("vote_average")
                        listItems.add(movieItems)
                    }
                    listMovie.postValue(listItems)
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
                listMovie.postValue(ArrayList())

            }
        })
    }
}
