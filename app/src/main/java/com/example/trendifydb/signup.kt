package com.example.trendifydb

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class signup : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etFullName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var db: TrendifyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = TrendifyDatabaseHelper(this)

        etEmail = findViewById(R.id.etemail)
        etFullName = findViewById(R.id.etfullname)
        etPassword = findViewById(R.id.etpassword)
        etConfirmPassword = findViewById(R.id.etcpassword)

        val registerBtn = findViewById<Button>(R.id.registerbtn)
        val goToSignIn = findViewById<TextView>(R.id.textView10)

        registerBtn.setOnClickListener {
            if (validateInputs()) {
                val fullName = etFullName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()

                val success = db.registerUser(fullName, email, password)

                if (success) {
                    Toast.makeText(this, "Registration successful! Please sign in.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, signin::class.java))
                    finish()
                } else {
                    etEmail.error = "This email is already registered"
                    Toast.makeText(this, "Email already registered. Try signing in instead.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        goToSignIn.setOnClickListener {
            startActivity(Intent(this, signin::class.java))
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (fullName.isEmpty()) {
            etFullName.error = "Full name is required"
            return false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email"
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            return false
        }
        if (confirmPassword != password) {
            etConfirmPassword.error = "Passwords do not match"
            return false
        }
        return true
    }
}