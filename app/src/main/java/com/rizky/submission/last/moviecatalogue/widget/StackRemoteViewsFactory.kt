package com.rizky.submission.last.moviecatalogue.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.helper.MovieMappingHelper
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.MovieFavoriteHelper


internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()
    private lateinit var movieFavoriteHelper: MovieFavoriteHelper

    override fun onCreate() {
        movieFavoriteHelper = MovieFavoriteHelper(mContext)
        movieFavoriteHelper.open()
    }

    override fun onDestroy() {
        movieFavoriteHelper.close()
    }

    @SuppressLint("Recycle")
    override fun onDataSetChanged() {

        val movieCursor = movieFavoriteHelper.queryAll()
        val movie = MovieMappingHelper.mapCursorToArrayList(movieCursor)
        val identityToken = Binder.clearCallingIdentity()
        mWidgetItems.clear()
        movie.forEachIndexed { _, movieItems ->
            val linkToLoad: String = if (movieItems.posterPath == "null") {
                "https://drive.google.com/uc?id=1ukFudMNIgwHQX56Zi73aToQjW3sTcqQv"
            } else {
                "https://image.tmdb.org/t/p/w92${movieItems.posterPath}"
            }
            mWidgetItems.add(
                Glide.with(mContext)
                    .asBitmap()
                    .load(linkToLoad)
                    .submit(612, 612)
                    .get()
            )
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getViewAt(position: Int): RemoteViews? {

        if (position >= mWidgetItems.size) {
            return loadingView
        }

        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(
            MovieFavoriteWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}