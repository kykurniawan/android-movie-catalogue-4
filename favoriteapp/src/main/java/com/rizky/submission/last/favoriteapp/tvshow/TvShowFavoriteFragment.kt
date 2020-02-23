package com.rizky.submission.last.favoriteapp.tvshow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizky.submission.last.favoriteapp.R
import com.rizky.submission.last.favoriteapp.database.DatabaseContract.TvShowFavoriteColumns.Companion.CONTENT_URI_TV_SHOW
import com.rizky.submission.last.favoriteapp.tvshow.TvShowMappingHelper.mapCursorToArrayList
import kotlinx.android.synthetic.main.tv_show_favorite_fragment.*

class TvShowFavoriteFragment : Fragment() {

    private lateinit var adapter: TvShowFavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tv_show_favorite_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        setFavoriteData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_error.visibility = View.GONE
        rv_tv_show.visibility = View.VISIBLE
        adapter = TvShowFavoriteAdapter()
        if (savedInstanceState == null) {
            setFavoriteData()
        } else {
            val list = savedInstanceState.getParcelableArrayList<TvShowItems>(EXTRA_STATE)
            if (list != null) {
                adapter.mData = list
            }
        }
    }


    @SuppressLint("Recycle")
    private fun setFavoriteData(){
        this.rv_tv_show.setHasFixedSize(true)
        this.rv_tv_show.layoutManager = LinearLayoutManager(this.context)
        this.rv_tv_show.adapter = this.adapter
        showLoading(true)
        val cursor =
            requireContext().contentResolver.query(CONTENT_URI_TV_SHOW, null, null, null, null)
        val tvShowList = cursor?.let { mapCursorToArrayList(it) }

        if (tvShowList != null) {
            if (tvShowList.size > 0){
                this.adapter.setData(tvShowList)
                showLoading(false)
                tv_error.visibility = View.GONE
                rv_tv_show.visibility = View.VISIBLE
            } else {
                this.adapter.setData(ArrayList())
                showLoading(false)
                tv_error.visibility = View.VISIBLE
                tv_error.text = resources.getString(R.string.no_data)
                rv_tv_show.visibility = View.GONE
            }
        } else {
            this.adapter.setData(ArrayList())
            showLoading(false)
            tv_error.visibility = View.VISIBLE
            tv_error.text = resources.getString(R.string.no_data)
            rv_tv_show.visibility = View.GONE
        }
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
}
