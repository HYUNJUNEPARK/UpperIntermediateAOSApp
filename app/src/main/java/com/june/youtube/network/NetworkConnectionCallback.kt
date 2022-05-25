package com.june.youtube.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkConnectionCallback(private val context: Context) : ConnectivityManager.NetworkCallback()  {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkRequest: NetworkRequest =  NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

    fun register() {
        connectivityManager.registerNetworkCallback(networkRequest, this)

        if (networkConnectionState() == null) {
            NetworkDialog(context).unConnectionDialog.show()
        }
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onLost(network: Network) {
        super.onLost(network)

        NetworkDialog(context).unConnectionDialog.show()
    }

    private fun networkConnectionState(): Network? {
        val network: Network? = connectivityManager.activeNetwork
        return network
    }
}