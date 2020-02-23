package com.rizky.submission.last.moviecatalogue.ui.tvshow

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.helper.FunctionHelper
import com.rizky.submission.last.moviecatalogue.helper.InternetConnection
import kotlinx.android.synthetic.main.movie_fragment.progressBar
import kotlinx.android.synthetic.main.tv_show_fragment.*

class TvShowFragment : Fragment() {

    private lateinit var adapter: TvShowAdapter
    private lateinit var tvShowViewModel: TvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.tv_show_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapter = TvShowAdapter()
        this.adapter.notifyDataSetChanged()
        this.rv_tv_show.setHasFixedSize(true)
        this.rv_tv_show.layoutManager = LinearLayoutManager(this.context)
        this.rv_tv_show.adapter = this.adapter

        this.tvShowViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TvShowViewModel::class.java)
        if (!InternetConnection.checkInternetConnection(requireContext())) {
            this.tvShowViewModel.setEmptyTvShow()
            FunctionHelper.showAlertDialog(
                requireContext(),
                resources.getString(R.string.network_error),
                resources.getString(R.string.no_internet_connection)
            )
            this.adapter.setData(ArrayList())
        } else {
            this.tvShowViewModel.setTvShow()
            showLoading(true)

            this.tvShowViewModel.getTvShow().observe(this.viewLifecycleOwner, Observer { tvShowItems ->
                if (tvShowItems.isNotEmpty()) {
                    this.adapter.setData(tvShowItems)
                    showLoading(false)
                } else {
                    this.adapter.setData(ArrayList())
                    showLoading(false)
                    FunctionHelper.showAlertDialog(
                        requireContext(),
                        resources.getString(R.string.no_data),
                        resources.getString(R.string.no_data_message)
                    )

                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val dialog = AlertDialog.Builder(requireContext())
                val input = EditText(requireContext())
                input.inputType = (InputType.TYPE_CLASS_TEXT)
                dialog
                    .setTitle("Search Tv")
                    .setView(input)
                    .apply {
                        setPositiveButton(
                            R.string.ok
                        ) { _, _ ->
                            if (input.text.isNotEmpty()) {
                                val query = input.text
                                val intent = Intent(context, TvShowSearchActivity::class.java)
                                intent.putExtra("query", query.toString().trim())
                                startActivity(intent)
                            } else {
                                Snackbar.make(
                                    fragment_tv_show,
                                    "Query cannot be empty",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        setNegativeButton(
                            R.string.cancel
                        ) { _, _ ->

                        }
                    }
                    .setCancelable(false)
                    .create()
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
