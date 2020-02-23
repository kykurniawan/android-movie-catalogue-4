package com.rizky.submission.last.favoriteapp.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizky.submission.last.favoriteapp.R
import com.rizky.submission.last.favoriteapp.database.DatabaseContract.MovieFavoriteColumns.Companion.CONTENT_URI_MOVIE
import com.rizky.submission.last.favoriteapp.movie.MovieMappingHelper.mapCursorToArrayList
import kotlinx.android.synthetic.main.movie_favorite_fragment.*

class MovieFavoriteFragment : Fragment() {

    private lateinit var adapter: MovieFavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_error.visibility = View.GONE
        rv_movie.visibility = View.VISIBLE

        adapter = MovieFavoriteAdapter()
        if (savedInstanceState == null) {
            setFavoriteData()
        } else {
            val list = savedInstanceState.getParcelableArrayList<MovieItems>(EXTRA_STATE)
            if (list != null) {
                adapter.mData = list
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setFavoriteData()
    }

    @SuppressLint("Recycle")
    private fun setFavoriteData() {
        this.rv_movie.setHasFixedSize(true)
        this.rv_movie.layoutManager = LinearLayoutManager(this.context)
        this.rv_movie.adapter = this.adapter
        showLoading(true)
        val cursor = requireContext().contentResolver.query(CONTENT_URI_MOVIE, null, null, null, null)
        val movieList = cursor?.let { mapCursorToArrayList(it) }
        println(cursor)
        if (movieList != null) {
            if (movieList.size > 0) {
                this.adapter.setData(movieList)
                showLoading(false)
                tv_error.visibility = View.GONE
                rv_movie.visibility = View.VISIBLE
            } else {
                this.adapter.setData(ArrayList())
                tv_error.text = resources.getString(R.string.no_data)
                tv_error.visibility = View.VISIBLE
                rv_movie.visibility = View.GONE
                showLoading(false)
            }
        } else {
            this.adapter.setData(ArrayList())
            tv_error.text = resources.getString(R.string.no_data)
            tv_error.visibility = View.VISIBLE
            rv_movie.visibility = View.GONE
            showLoading(false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mData)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
