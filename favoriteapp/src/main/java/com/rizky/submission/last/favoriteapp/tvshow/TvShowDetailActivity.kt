package com.rizky.submission.last.favoriteapp.tvshow

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
import com.rizky.submission.last.favoriteapp.database.DatabaseContract
import kotlinx.android.synthetic.main.activity_tv_show_detail.*

class TvShowDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TV_SHOW = "extra_tv_show"
    }

    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)
        val tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShowItems
        val actionbar = supportActionBar
        actionbar?.title = tvShow.name
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayUseLogoEnabled(true)

        showLoading(true)

        val posterLink: String = if (tvShow.posterPath == "null") {
            "https://drive.google.com/uc?id=1ukFudMNIgwHQX56Zi73aToQjW3sTcqQv"
        } else {
            "https://image.tmdb.org/t/p/w92${tvShow.posterPath}"
        }
        val backdropLink: String = if (tvShow.backdropPath == "null") {
            "https://drive.google.com/uc?id=1ukFudMNIgwHQX56Zi73aToQjW3sTcqQv"
        } else {
            "https://image.tmdb.org/t/p/w92${tvShow.backdropPath}"
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
                    Toast.makeText(this@TvShowDetailActivity, "Gagal memuat gambar", Toast.LENGTH_SHORT)
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
                    Toast.makeText(this@TvShowDetailActivity, "Load Berhasil", Toast.LENGTH_SHORT).show()
                    return false
                }
            })
            .apply(RequestOptions().override(1920, 1080))
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
                    Toast.makeText(this@TvShowDetailActivity, "Gagal memuat gambar", Toast.LENGTH_SHORT)
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
                    Toast.makeText(this@TvShowDetailActivity, "Load Berhasil", Toast.LENGTH_SHORT).show()
                    return false
                }
            })
            .placeholder(CircularProgressDrawable(this))
            .apply(RequestOptions().override(400, 510))
            .into(img_poster)

        tv_tv_show_name.text = tvShow.name
        tv_air_date.text = getString(R.string.first_air_date, tvShow.firstAirDate)
        tv_vote.text = getString(R.string.vote_count, tvShow.voteCount)
        tv_overview.text = tvShow.overview
        tv_rating.text = getString(R.string.vote_average, tvShow.voteAverage)
        tv_popularity.text = getString(R.string.popularity, tvShow.popularity)

        btn_set_favorite.setOnClickListener {
            deleteFromFavorite(this, tvShow)
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

    private fun deleteFromFavorite(context: Context, tvShowItems: TvShowItems) {
        uriWithId = Uri.parse(DatabaseContract.TvShowFavoriteColumns.CONTENT_URI_TV_SHOW.toString() + "/" + tvShowItems.id)
        val result = contentResolver.delete(uriWithId, null, null)
        if (result > 0) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("items", tvShowItems.name)
            startActivity(intent)
        }
    }
}
