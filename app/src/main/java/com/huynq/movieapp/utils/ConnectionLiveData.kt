package com.huynq.movieapp.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData


class ConnectionLiveData(private val connection: ConnectivityManager) :LiveData<Boolean>(){
    constructor(application: Application): this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object: ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: android.net.Network) {
            super.onAvailable(network)
            postValue(true)
    }
        override fun onLost(network: android.net.Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        connection.registerNetworkCallback(builder.build(),networkCallback)
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInactive() {
        super.onInactive()
        connection.unregisterNetworkCallback(networkCallback)
    }
}