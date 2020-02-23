package com.rizky.submission.last.favoriteapp.movie

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.rizky.submission.last.favoriteapp.MainActivity
import com.rizky.submission.last.favoriteapp.R
import com.rizky.submission.last.favoriteapp.database.DatabaseContract.MovieFavoriteColumns.Companion.CONTENT_URI_MOVIE
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as MovieItems
        val actionbar = supportActionBar
        actionbar?.title = movie.title
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayUseLogoEnabled(true)

        showLoading(true)

        val posterLink: String = if (movie.posterPath == "null") {
            "https://drive.google.com/uc?id=1ukFudMNIgwHQX56Zi73aToQjW3sTcqQv"
        } else {
            "https://image.tmdb.org/t/p/w92${movie.posterPath}"
        }
        val backdropLink: String = if (movie.backdropPath == "null") {
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

        btn_set_favorite.setOnClickListener {
            deleteFromFavorite(this, movie)
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

    private fun deleteFromFavorite(context: Context, movieItems: MovieItems) {
        uriWithId = Uri.parse(CONTENT_URI_MOVIE.toString() + "/" + movieItems.id)
        val result = contentResolver.delete(uriWithId, null, null)
        if (result > 0) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("items", movieItems.title)
            startActivity(intent)
        }
    }
}
