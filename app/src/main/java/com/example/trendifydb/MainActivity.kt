package com.example.trendifydb

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var receiver: AirplaneModeChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val signupBtn = findViewById<android.widget.Button>(R.id.signupbtn)
        val signinBtn = findViewById<android.widget.Button>(R.id.signinbtn)

        signupBtn.setOnClickListener {
            startActivity(Intent(this, signup::class.java))
        }

        signinBtn.setOnClickListener {
            startActivity(Intent(this, signin::class.java))
        }

        receiver = AirplaneModeChangeReceiver()
    }

    override fun onStart() {
        super.onStart()

        receiver = AirplaneModeChangeReceiver()

        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, filter)
        }
    }

    override fun onStop() {
        unregisterReceiver(receiver)
        super.onStop()
    }

}
