package com.example.trendifydb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class signin : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRememberMe: CheckBox
    private lateinit var db: TrendifyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = TrendifyDatabaseHelper(this)

        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etpassword)
        cbRememberMe = findViewById(R.id.cbRememberMe)

        val loginBtn = findViewById<Button>(R.id.loginbtn)
        val forgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val goToSignUp = findViewById<TextView>(R.id.textView5)

        // Pre-fill email if it was remembered on a previous login
        val rememberedEmail = SessionManager.getRememberedEmail(this)
        if (rememberedEmail != null) {
            etEmail.setText(rememberedEmail)
            cbRememberMe.isChecked = true
        }

        loginBtn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = db.loginUser(email, password)

            if (user != null) {
                if (cbRememberMe.isChecked) {
                    SessionManager.setRememberedEmail(this, email)
                } else {
                    SessionManager.clearRememberedEmail(this)
                }

                SessionManager.setLoggedInEmail(this, user.email)

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, home::class.java)
                intent.putExtra("USER_NAME", user.name)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        forgotPassword.setOnClickListener {
            Toast.makeText(this, "Password reset link sent to your email (demo)", Toast.LENGTH_SHORT).show()
        }

        goToSignUp.setOnClickListener {
            startActivity(Intent(this, signup::class.java))
            finish()
        }
    }
}