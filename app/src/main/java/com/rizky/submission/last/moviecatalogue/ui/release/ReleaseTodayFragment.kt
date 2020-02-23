package com.rizky.submission.last.moviecatalogue.ui.release

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.helper.FunctionHelper
import com.rizky.submission.last.moviecatalogue.helper.InternetConnection
import kotlinx.android.synthetic.main.release_today_fragment.*

class ReleaseTodayFragment : Fragment() {

    private lateinit var adapter: ReleaseTodayAdapter
    private lateinit var viewModel: ReleaseTodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.release_today_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        rv_movie_release_today.visibility = View.VISIBLE
        tv_error.visibility = View.GONE
    }

    private fun setData(){
        this.adapter = ReleaseTodayAdapter()
        this.adapter.notifyDataSetChanged()
        this.rv_movie_release_today.setHasFixedSize(true)
        this.rv_movie_release_today.layoutManager = LinearLayoutManager(this.context)
        this.rv_movie_release_today.adapter = this.adapter

        this.viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ReleaseTodayViewModel::class.java)
        if (!InternetConnection.checkInternetConnection(requireContext())){
            this.viewModel.setEmptyMovieReleaseToday()
            FunctionHelper.showAlertDialog(
                requireContext(),
                resources.getString(R.string.network_error),
                resources.getString(R.string.no_internet_connection)
            )
            this.adapter.setData(ArrayList())
            rv_movie_release_today.visibility = View.GONE
            tv_error.text = resources.getString(R.string.network_error)
            tv_error.visibility = View.VISIBLE
        } else {
            this.viewModel.setMovieReleaseToday()
            showLoading(true)

            this.viewModel.getMovieReleaseToday().observe(this.viewLifecycleOwner, Observer { movieItems ->
                if (movieItems.isNotEmpty()) {
                    this.adapter.setData(movieItems)
                    rv_movie_release_today.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    showLoading(false)
                } else {
                    this.adapter.setData(ArrayList())
                    showLoading(false)
                    FunctionHelper.showAlertDialog(
                        requireContext(),
                        resources.getString(R.string.no_data),
                        resources.getString(R.string.no_data_message)
                    )
                    rv_movie_release_today.visibility = View.GONE
                    tv_error.text = resources.getString(R.string.no_data)
                    tv_error.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
