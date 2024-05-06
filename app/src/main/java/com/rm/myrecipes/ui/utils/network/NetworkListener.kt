package com.rm.myrecipes.ui.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NetworkListener @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(): MutableStateFlow<Boolean> {
        var isConnected = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        networkCapabilities?.let {
            if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ||
                (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P))) {
                isConnected = true
            }
        }
        isNetworkAvailable.value = isConnected
        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}