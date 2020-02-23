package com.rizky.submission.last.moviecatalogue.ui.favorite.tvshow

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
import com.rizky.submission.last.moviecatalogue.ui.tvshow.TvShowDetailActivity
import com.rizky.submission.last.moviecatalogue.ui.tvshow.TvShowItems
import kotlinx.android.synthetic.main.item_row_tv_show.view.*

class TvShowFavoriteAdapter: RecyclerView.Adapter<TvShowFavoriteAdapter.TvShowFavoriteViewHolder>() {

    var mData = ArrayList<TvShowItems>()
    fun setData(items: ArrayList<TvShowItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): TvShowFavoriteViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_tv_show, viewGroup, false)
        return TvShowFavoriteViewHolder(mView)
    }

    override fun onBindViewHolder(tvShowFavoriteViewHolder: TvShowFavoriteViewHolder, position: Int) {
        tvShowFavoriteViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class TvShowFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShowItems: TvShowItems) {
            with(itemView) {
                val poster = tvShowItems.posterPath
                val posterLink = "https://image.tmdb.org/t/p/w780$poster"
                Glide.with(itemView.context)
                    .load(posterLink)
                    .placeholder(CircularProgressDrawable(this.context))
                    .apply(RequestOptions().override(350, 550))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(img_tv_show_poster)
                tv_tv_show_name.text = tvShowItems.name
                tv_first_air_date.text = tvShowItems.firstAirDate

                card_view.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback{
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(context, TvShowDetailActivity::class.java)
                                intent.putExtra(TvShowDetailActivity.EXTRA_TVSHOW, tvShowItems)
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