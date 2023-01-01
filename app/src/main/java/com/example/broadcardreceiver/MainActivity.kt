package com.example.broadcardreceiver

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), MyReceiver.ConnectivityReceiverListener {

    private val powerReciever = MyReceiver()
    lateinit var myImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myImage = findViewById(R.id.imageView)

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        filter.also {
            registerReceiver(powerReciever, it)
        }


        /**
         * Intents to get the image from gallary and load into our application.
         * we need to add permission in manifest
         * this is the type of static broadcast receiver
         * used until API 26
         */
        val intent: Intent = this@MainActivity.getIntent()
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            myImage.setImageURI(intent.getParcelableExtra(Intent.EXTRA_STREAM))
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(powerReciever)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        val parentLayout = findViewById<View>(android.R.id.content)
        if (!isConnected) {
            Snackbar.make(parentLayout, "You are offline", Snackbar.LENGTH_LONG)
                .show() //Assume "rootLayout" as the root layout of every activity.
        } else {
            Snackbar.make(parentLayout, "You are Online", Snackbar.LENGTH_LONG)
                .show() //Assume "rootLayout" as the root layout of every activity.

        }
    }

    override fun onResume() {
        super.onResume()
        MyReceiver.connectivityReceiverListener = this
    }
}
