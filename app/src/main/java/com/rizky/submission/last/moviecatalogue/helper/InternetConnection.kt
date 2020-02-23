package com.rizky.submission.last.moviecatalogue.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

@Suppress("DEPRECATION")
object InternetConnection {

    fun checkInternetConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        return activeNetwork?.isConnectedOrConnecting == true
    }

}