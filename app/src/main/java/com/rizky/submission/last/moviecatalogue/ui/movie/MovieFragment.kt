package com.rizky.submission.last.moviecatalogue.ui.movie

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
import kotlinx.android.synthetic.main.movie_fragment.*

class MovieFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
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
                    .setTitle("Search Movie")
                    .setView(input)
                    .apply {
                        setPositiveButton(
                            R.string.ok
                        ) { _, _ ->
                            if (input.text.isNotEmpty()) {
                                val query = input.text
                                val intent = Intent(context, MovieSearchActivity::class.java)
                                intent.putExtra("query", query.toString().trim())
                                startActivity(intent)
                            } else {
                                Snackbar.make(
                                    fragment_movie,
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

    private fun setData() {
        this.adapter = MovieAdapter()
        this.adapter.notifyDataSetChanged()
        this.rv_movie.setHasFixedSize(true)
        this.rv_movie.layoutManager = LinearLayoutManager(this.context)
        this.rv_movie.adapter = this.adapter

        this.movieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)
        if (!InternetConnection.checkInternetConnection(requireContext())) {
            this.movieViewModel.setEmptyMovie()
            FunctionHelper.showAlertDialog(
                requireContext(),
                resources.getString(R.string.network_error),
                resources.getString(R.string.no_internet_connection)
            )
            this.adapter.setData(ArrayList())
        } else {
            this.movieViewModel.setMovie()
            showLoading(true)

            this.movieViewModel.getMovie().observe(this.viewLifecycleOwner, Observer { movieItems ->
                if (movieItems.isNotEmpty()) {
                    this.adapter.setData(movieItems)
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


}
