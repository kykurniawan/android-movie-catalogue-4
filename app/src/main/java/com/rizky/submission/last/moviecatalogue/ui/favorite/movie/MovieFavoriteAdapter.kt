package com.rizky.submission.last.moviecatalogue.ui.favorite.movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizky.submission.last.moviecatalogue.CustomOnItemClickListener
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.ui.movie.MovieDetailActivity
import com.rizky.submission.last.moviecatalogue.ui.movie.MovieItems
import kotlinx.android.synthetic.main.item_row_movie.view.*

class MovieFavoriteAdapter: RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteViewHolder>() {

    var mData = ArrayList<MovieItems>()
    fun setData(items: ArrayList<MovieItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MovieFavoriteViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_movie, viewGroup, false)
        return MovieFavoriteViewHolder(mView)
    }

    override fun onBindViewHolder(movieFavoriteViewHolder: MovieFavoriteViewHolder, position: Int) {
        movieFavoriteViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class MovieFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieItems: MovieItems) {
            with(itemView) {
                val poster = movieItems.posterPath
                val posterLink = "https://image.tmdb.org/t/p/w780$poster"
                Glide.with(itemView.context)
                    .load(posterLink)
                    .placeholder(CircularProgressDrawable(this.context))
                    .apply(RequestOptions().override(350, 550))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(img_movie_poster)
                tv_movie_title.text = movieItems.title
                tv_movie_release_date.text = movieItems.releaseDate

                card_view.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback{
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(context, MovieDetailActivity::class.java)
                                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movieItems)
                                intent.putExtra("from", "favorite")
                                context.startActivity(intent)
                            }
                        }
                    )
                )

            }
        }
    }
}