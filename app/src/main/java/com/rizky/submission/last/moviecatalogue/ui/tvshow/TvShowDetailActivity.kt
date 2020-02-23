package com.rizky.submission.last.moviecatalogue.ui.tvshow

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
import com.rizky.submission.last.moviecatalogue.helper.TvShowMappingHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.TvShowFavoriteHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.tvshow.TvShowFavoriteViewModel
import kotlinx.android.synthetic.main.activity_tv_show_detail.*

class TvShowDetailActivity : AppCompatActivity() {

    private lateinit var tvShowFavoriteViewModel: TvShowFavoriteViewModel
    private lateinit var tvShowFavoriteHelper: TvShowFavoriteHelper

    companion object {
        const val EXTRA_TVSHOW = "extra_tvshow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)
        this.tvShowFavoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TvShowFavoriteViewModel::class.java)
        tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(applicationContext)
        tvShowFavoriteHelper.open()

        val tvShow = intent.getParcelableExtra(EXTRA_TVSHOW) as TvShowItems

        showLoading(true)

        val backdrop = tvShow.backdropPath
        val poster = tvShow.posterPath
        val backdropLink = "https://image.tmdb.org/t/p/original$backdrop"
        val posterLink = "https://image.tmdb.org/t/p/original$poster"

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

        val actionbar = supportActionBar
        actionbar?.title = tvShow.name
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayUseLogoEnabled(true)

        val cursor = tvShowFavoriteHelper.queryById(tvShow.id.toString())
        val tvShowFavorite = TvShowMappingHelper.mapCursorToArrayList(cursor)

        if (intent.getStringExtra("from") == "favorite") {
            btn_set_favorite.setOnClickListener{
                deleteFromFavorite(tvShow)
            }
            btn_set_favorite.text = resources.getString(R.string.delete_from_favorite)
        } else {
            if(tvShowFavorite.isEmpty()) {
                btn_set_favorite.text = resources.getString(R.string.add_to_favorite)
                btn_set_favorite.setOnClickListener{
                    saveToFavorite(tvShow)
                }
            } else {
                btn_set_favorite.text = resources.getString(R.string.delete_from_favorite)
                btn_set_favorite.setOnClickListener {
                    deleteFromFavorite(tvShow)
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

    private fun saveToFavorite(tvShow: TvShowItems) {
        val result = tvShowFavoriteViewModel.saveTvShowToFavorite(tvShow, tvShowFavoriteHelper)
        if (result > 0 ) {
            val snackBar =
                Snackbar.make(tvshow_detail, R.string.added_to_favorite, Snackbar.LENGTH_LONG)
            snackBar.show()
            btn_set_favorite.text = resources.getString(R.string.delete_from_favorite)
            btn_set_favorite.setOnClickListener {
                deleteFromFavorite(tvShow)
            }
        }
    }

    private fun deleteFromFavorite(tvShow: TvShowItems) {
        val result = tvShowFavoriteViewModel.deleteTvShowFromFavorite(tvShow, tvShowFavoriteHelper)
        if (result > 0 ){
            val snackBar = Snackbar.make(tvshow_detail, resources.getString(R.string.deleted_from_favorite), Snackbar.LENGTH_LONG)
            snackBar.show()
            btn_set_favorite.text = resources.getString(R.string.add_to_favorite)
            btn_set_favorite.setOnClickListener {
                saveToFavorite(tvShow)
            }
        }
    }

}
