package com.rizky.submission.last.favoriteapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
            view_pager.adapter = sectionsPagerAdapter
            tabs.setupWithViewPager(view_pager)
            supportActionBar?.elevation = 0f
        } catch (e: Exception) {
            Log.d("Exception", e.message.toString())
        }

        if (intent.extras != null){
            val deletedItems = intent.getStringExtra("items")
            Toast.makeText(this, getString(R.string.deleted_from_favorite, deletedItems), Toast.LENGTH_LONG).show()
        }
    }
}
