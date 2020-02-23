package com.rizky.submission.last.moviecatalogue

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rizky.submission.last.moviecatalogue.helper.InternetConnection
import kotlinx.android.synthetic.main.error_layout.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val internet = InternetConnection.checkInternetConnection(this)
        if (!internet) {
            setContentView(R.layout.error_layout)

            error_text.setText(R.string.internet_not_connected)
            refresh_btn.setOnClickListener {
                finish()
                startActivity(intent)
            }

            val dialog = AlertDialog.Builder(this)
            dialog
                .setMessage(R.string.internet_not_connected_message)
                .setTitle(R.string.internet_not_connected)
                .apply {
                    setPositiveButton(
                        R.string.ok
                    ) { _, _ ->
                        // User clicked OK button
                        val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
                        startActivity(intent)
                    }
                    setNegativeButton(
                        R.string.cancel
                    ) { _, _ ->
                        // User cancelled the dialog
                        moveTaskToBack(true)
                        exitProcess(-1)
                    }
                }
                .setCancelable(false)
                .create()
                .show()
        } else {

            setContentView(R.layout.activity_main)
            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_movie,
                    R.id.navigation_tvshow,
                    R.id.navigation_favorite,
                    R.id.navigation_release_today,
                    R.id.navigation_setting
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

}
