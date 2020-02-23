package com.rizky.submission.last.moviecatalogue.ui.favorite.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.ui.favorite.database.TvShowFavoriteHelper
import com.rizky.submission.last.moviecatalogue.ui.tvshow.TvShowItems
import kotlinx.android.synthetic.main.movie_fragment.progressBar
import kotlinx.android.synthetic.main.tv_show_favorite_fragment.*

class TvShowFavoriteFragment : Fragment() {

    private lateinit var adapter: TvShowFavoriteAdapter
    private lateinit var tvShowFavoriteHelper: TvShowFavoriteHelper
    private lateinit var tvShowFavoriteViewModel: TvShowFavoriteViewModel

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tv_show_favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(requireContext())
        tvShowFavoriteHelper.open()

        this.adapter = TvShowFavoriteAdapter()
        this.adapter.notifyDataSetChanged()

        if (savedInstanceState == null){
            setFavoriteData()
        } else {
            val list = savedInstanceState.getParcelableArrayList<TvShowItems>(EXTRA_STATE)
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
        tvShowFavoriteHelper.close()
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
        this.rv_tv_show.setHasFixedSize(true)
        this.rv_tv_show.layoutManager = LinearLayoutManager(this.context)
        this.rv_tv_show.adapter = this.adapter
        this.tvShowFavoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TvShowFavoriteViewModel::class.java)

        this.tvShowFavoriteViewModel.setTvShowFavorite(tvShowFavoriteHelper)
        showLoading(true)

        this.tvShowFavoriteViewModel.getTvShowFavorite().observe(this.viewLifecycleOwner, Observer { tvShowItems ->
            if (tvShowItems.isNotEmpty()) {
                this.adapter.setData(tvShowItems)
                showLoading(false)
                tv_error.visibility = View.GONE
                rv_tv_show.visibility = View.VISIBLE
            } else {
                this.adapter.setData(ArrayList())
                tv_error.text = resources.getString(R.string.no_data)
                tv_error.visibility = View.VISIBLE
                rv_tv_show.visibility = View.GONE
                showLoading(false)
            }
        })
    }

}
