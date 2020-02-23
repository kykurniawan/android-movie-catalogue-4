package com.rizky.submission.last.moviecatalogue.ui.favorite.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.MovieFavoriteHelper
import com.rizky.submission.last.moviecatalogue.ui.movie.MovieItems
import kotlinx.android.synthetic.main.movie_favorite_fragment.*
import kotlinx.android.synthetic.main.movie_fragment.progressBar
import kotlinx.android.synthetic.main.movie_fragment.rv_movie

class MovieFavoriteFragment : Fragment() {

    private lateinit var adapter: MovieFavoriteAdapter
    private lateinit var movieFavoriteHelper: MovieFavoriteHelper
    private lateinit var movieFavoriteViewModel: MovieFavoriteViewModel

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
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(requireContext())
        movieFavoriteHelper.open()

        this.adapter = MovieFavoriteAdapter()
        this.adapter.notifyDataSetChanged()
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

    override fun onDestroy() {
        super.onDestroy()
        movieFavoriteHelper.close()
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

    private fun setFavoriteData(){
        this.rv_movie.setHasFixedSize(true)
        this.rv_movie.layoutManager = LinearLayoutManager(this.context)
        this.rv_movie.adapter = this.adapter
        this.movieFavoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieFavoriteViewModel::class.java)

        this.movieFavoriteViewModel.setMovieFavorite(movieFavoriteHelper)
        showLoading(true)

        this.movieFavoriteViewModel.getMovieFavorite().observe(this.viewLifecycleOwner, Observer { movieItems ->
            if (movieItems.isNotEmpty()) {
                this.adapter.setData(movieItems)
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
        })
    }



}
