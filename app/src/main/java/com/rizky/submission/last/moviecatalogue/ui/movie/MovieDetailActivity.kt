package com.rizky.submission.last.moviecatalogue.ui.movie

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.helper.MovieMappingHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.MovieFavoriteHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.movie.MovieFavoriteViewModel
import com.rizky.submission.last.moviecatalogue.widget.MovieFavoriteWidget
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetailActivity : AppCompatActivity() {


    private lateinit var movieFavoriteViewModel: MovieFavoriteViewModel
    private lateinit var movieFavoriteHelper: MovieFavoriteHelper

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        this.movieFavoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieFavoriteViewModel::class.java)
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(applicationContext)
        movieFavoriteHelper.open()

        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as MovieItems

        showLoading(true)

        val posterLink: String = if(movie.posterPath == "null"  ){
            "https://drive.google.com/uc?id=1ukFudMNIgwHQX56Zi73aToQjW3sTcqQv"
        } else {
            "https://image.tmdb.org/t/p/w92${movie.posterPath}"
        }
        val backdropLink: String = if(movie.backdropPath == "null"  ){
            "https://drive.google.com/uc?id=1ukFudMNIgwHQX56Zi73aToQjW3sTcqQv"
        } else {
            "https://image.tmdb.org/t/p/w92${movie.backdropPath}"
        }

        Glide
            .with(this)
            .load(backdropLink)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    showLoading(false)
                    Toast.makeText(
                        this@MovieDetailActivity,
                        "Gagal memuat gambar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    showLoading(false)
                    Toast.makeText(this@MovieDetailActivity, "Load Berhasil", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }
            })
            .apply(RequestOptions().override(1920, 1080))
            .error(R.drawable.ic_launcher_foreground)
            .into(img_backdrop)

        Glide
            .with(this)
            .load(posterLink)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(
                        this@MovieDetailActivity,
                        "Gagal memuat gambar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(this@MovieDetailActivity, "Load Berhasil", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }
            })
            .placeholder(CircularProgressDrawable(this))
            .error(R.drawable.ic_launcher_foreground)
            .apply(RequestOptions().override(400, 510))
            .into(img_poster)

        tv_title.text = movie.title
        tv_release.text = getString(R.string.release_date, movie.releaseDate)
        tv_vote.text = getString(R.string.vote_count, movie.voteCount)
        tv_overview.text = movie.overview
        tv_rating.text = getString(R.string.vote_average, movie.voteAverage)
        tv_popularity.text = getString(R.string.popularity, movie.popularity)


        val actionbar = supportActionBar
        actionbar?.title = movie.title
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayUseLogoEnabled(true)

        val cursor = movieFavoriteHelper.queryById(movie.id.toString())
        val movieFavorite = MovieMappingHelper.mapCursorToArrayList(cursor)

        if (intent.getStringExtra("from") == "favorite") {
            btn_set_favorite
                .setOnClickListener {
                    deleteFromFavorite(movie)
                }
            btn_set_favorite.text = resources.getString(R.string.delete_from_favorite)
        } else {
            if (movieFavorite.isEmpty()) {
                btn_set_favorite.text = resources.getString(R.string.add_to_favorite)
                btn_set_favorite.setOnClickListener {
                    saveToFavorite(movie)
                }
            } else {
                btn_set_favorite.text = resources.getString(R.string.delete_from_favorite)
                btn_set_favorite.setOnClickListener {
                    deleteFromFavorite(movie)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun saveToFavorite(movie: MovieItems) {
        val result = movieFavoriteViewModel.saveMovieToFavorite(movie, movieFavoriteHelper)
        if (result > 0) {
            val snackBar =
                Snackbar.make(movie_detail, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
            snackBar.show()
            btn_set_favorite.text = resources.getString(R.string.delete_from_favorite)
            btn_set_favorite.setOnClickListener {
                deleteFromFavorite(movie)
            }
            updateWidget()
        }
    }

    private fun deleteFromFavorite(movie: MovieItems) {
        val result = movieFavoriteViewModel.deleteMovieFromFavorite(movie, movieFavoriteHelper)
        if (result > 0) {
            val snackBar =
                Snackbar.make(movie_detail, R.string.deleted_from_favorite, Snackbar.LENGTH_LONG)
            snackBar.show()
            btn_set_favorite.text = resources.getString(R.string.add_to_favorite)
            btn_set_favorite.setOnClickListener {
                saveToFavorite(movie)
            }
            updateWidget()
        }
    }

    private fun updateWidget() {
        val context: Context = applicationContext
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisWidget = ComponentName(context, MovieFavoriteWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        appWidgetIds.forEach {
            appWidgetManager.notifyAppWidgetViewDataChanged(it, R.id.stack_view)
        }
    }

}
