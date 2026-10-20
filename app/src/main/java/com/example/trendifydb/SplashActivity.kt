package com.example.trendifydb

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val splashDuration = 2200L // milliseconds before moving to the main screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.ivSplashLogo)
        val appName = findViewById<TextView>(R.id.tvSplashName)

        // Simple fade-in + scale-up animation for the logo and app name
        val fadeScale = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        logo.startAnimation(fadeScale)
        appName.startAnimation(fadeScale)

        // Wait, then move to MainActivity and remove Splash from the back stack
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashDuration)
    }
}