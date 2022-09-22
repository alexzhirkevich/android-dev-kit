package com.github.alexzhirkevich.devkit

import android.Manifest
import android.content.Context
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.github.alexzhirkevich.devkit.communication.MutableStateCommunication
import com.github.alexzhirkevich.devkit.communication.Releasable
import com.github.alexzhirkevich.devkit.communication.StateCommunication
import android.net.ConnectivityManager.NetworkCallback
interface ConnectivityManager : Releasable {

    val isNetworkConnected: StateCommunication<Boolean>

    class Internet @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE) constructor(
        context: Context,
        override val isNetworkConnected: MutableStateCommunication<Boolean>,
    ) : ConnectivityManager {

        private var wifiAvailable = false
        private var cellAvailable = false

        private var wifiCallback : NetworkCallback? = null
        private var cellCallback : NetworkCallback? = null

        private val service: android.net.ConnectivityManager
        init {
            service = (context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager)

            wifiCallback = service.register(NetworkCapabilities.TRANSPORT_WIFI) {
                wifiAvailable = it
                isNetworkConnected.map(wifiAvailable || cellAvailable)
            }
            cellCallback = service.register(NetworkCapabilities.TRANSPORT_CELLULAR) {
                cellAvailable = it
                isNetworkConnected.map(wifiAvailable || cellAvailable)
            }
        }

        fun finalize(){
            release()
        }

        override fun release() {
            listOfNotNull(wifiCallback,cellCallback)
                .forEach(service::unregisterNetworkCallback)
        }
    }
}


@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
private fun android.net.ConnectivityManager.register(
    transport : Int,
    set : (Boolean) -> Unit
) : NetworkCallback {
    val callback = object  : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            set(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            set(false)
        }
    }
    registerNetworkCallback(
        NetworkRequest.Builder()
            .addTransportType(transport)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build(),
        callback
    )
    return callback
}