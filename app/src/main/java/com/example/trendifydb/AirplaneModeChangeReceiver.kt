package com.example.trendifydb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

class AirplaneModeChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {

            val isAirplaneModeOn = Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) != 0

            val message = if (isAirplaneModeOn) {
                "Airplane Mode Enabled"
            } else {
                "Airplane Mode Disabled"
            }

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}