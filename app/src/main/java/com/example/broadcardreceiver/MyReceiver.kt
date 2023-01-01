package com.example.broadcardreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast


class MyReceiver : BroadcastReceiver() {

    var isAirplaneModeEnabled: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(
                isConnectedOrConnecting(
                    context!!
                )
            )
        }

        isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return

        val level: Int = intent!!.getIntExtra("level", 0)
        var batteryLevel = "$level%"
        val intentAction = intent!!.action

        /**
         * Here we are checking all the broadcast action and send the specific message to toast method
         */
        if (intentAction != null) {
            var toastMessage: String = ""
            when (intentAction) {
                Intent.ACTION_POWER_CONNECTED -> toastMessage = "Power is connected"
                Intent.ACTION_POWER_DISCONNECTED -> toastMessage = "Power is disconected"
                Intent.ACTION_BATTERY_CHANGED -> toastMessage = batteryLevel
                Intent.ACTION_AIRPLANE_MODE_CHANGED -> if (isAirplaneModeEnabled) toastMessage =
                    "Flight Mode is On"
                else "Flight Mode is off"
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}