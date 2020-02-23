package com.rizky.submission.last.moviecatalogue.ui.tvshow

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
import kotlinx.android.synthetic.main.item_row_tv_show.view.*

class TvShowSearchAdapter: RecyclerView.Adapter<TvShowSearchAdapter.TvShowSearchViewHolder>() {

    private val mData = ArrayList<TvShowItems>()
    fun setData(items: ArrayList<TvShowItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): TvShowSearchViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_tv_show, viewGroup, false)
        return TvShowSearchViewHolder(mView)
    }

    override fun onBindViewHolder(tvShowSearchViewHolder: TvShowSearchViewHolder, position: Int) {
        tvShowSearchViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class TvShowSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(tvShowItems: TvShowItems) {
            with(itemView) {
                val poster = tvShowItems.posterPath
                val posterLink = "https://image.tmdb.org/t/p/w780$poster"
                Glide.with(itemView.context)
                    .load(posterLink)
                    .placeholder(CircularProgressDrawable(this.context))
                    .apply(RequestOptions().override(350, 550))
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
                                intent.putExtra("from", "tvshow")
                                context.startActivity(intent)
                            }
                        }
                    )
                )

            }
        }
    }
}