package com.rizky.submission.last.moviecatalogue.ui.movie

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.helper.FunctionHelper
import com.rizky.submission.last.moviecatalogue.helper.InternetConnection
import kotlinx.android.synthetic.main.activity_movie_search.*

class MovieSearchActivity : AppCompatActivity() {

    private lateinit var adapter: MovieSearchAdapter
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)
        val query = intent.getStringExtra("query")
        searchMovieItems(query)
        val actionbar = supportActionBar
        setActionBarTitle("Search for: $query")
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayUseLogoEnabled(true)

        rv_movie.visibility = View.GONE
        tv_error.setText(R.string.searching)
        tv_error.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val dialog = AlertDialog.Builder(this)
                val input = EditText(this)
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
                                searchMovieItems(query.toString())
                                setActionBarTitle("Search for: $query")
                                rv_movie.visibility = View.GONE
                                tv_error.setText(R.string.searching)
                                tv_error.visibility = View.VISIBLE
                            } else {
                                Snackbar.make(
                                    search_layout,
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

    private fun searchMovieItems(query: String?) {

        this.adapter = MovieSearchAdapter()
        this.adapter.notifyDataSetChanged()
        this.rv_movie.setHasFixedSize(true)
        this.rv_movie.layoutManager = LinearLayoutManager(this)
        this.rv_movie.adapter = this.adapter

        this.movieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)
        if (!InternetConnection.checkInternetConnection(this)) {
            this.movieViewModel.setEmptyMovie()
            FunctionHelper.showAlertDialog(
                this,
                resources.getString(R.string.network_error),
                resources.getString(R.string.no_internet_connection)
            )
            progressBar.visibility = View.GONE
            rv_movie.visibility = View.GONE
            tv_error.setText(R.string.no_internet_connection)
            tv_error.visibility = View.VISIBLE
            this.adapter.setData(ArrayList())
        } else {
            this.movieViewModel.searchMovie(query)
            this.movieViewModel.getMovie().observe(this, Observer { movieItems ->
                if (movieItems.isNotEmpty()) {
                    this.adapter.setData(movieItems)
                    rv_movie.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    progressBar.visibility = View.GONE
                } else {
                    this.adapter.setData(ArrayList())
                    progressBar.visibility = View.GONE
                    rv_movie.visibility = View.GONE
                    tv_error.setText(R.string.no_data)
                    tv_error.visibility = View.VISIBLE
                }
            })
        }

    }

    private fun setActionBarTitle(title: String) {
        val actionbar = supportActionBar
        actionbar?.title = title
    }
}
