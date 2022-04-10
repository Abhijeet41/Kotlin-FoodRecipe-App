package com.abhi41.foodrecipe.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class CheckConnection {
    companion object{
        fun  hasInternetConnection(application: Application): Boolean {
            val connectivityManager = application
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capebilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capebilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capebilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capebilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }

}