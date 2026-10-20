package com.example.trendifydb

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Status bar color matches the toolbar
        window.statusBarColor = Color.rgb(81, 69, 229)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Shopify"
        toolbar.setTitleTextColor(Color.WHITE)

        // Move the toolbar below the status bar (kept exactly as working before)
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val params = view.layoutParams
            val toolbarHeight = resources.getDimensionPixelSize(
                androidx.appcompat.R.dimen.abc_action_bar_default_height_material
            )
            params.height = toolbarHeight + statusBarHeight
            view.layoutParams = params
            view.setPadding(view.paddingLeft, statusBarHeight, view.paddingRight, view.paddingBottom)
            insets
        }
        ViewCompat.requestApplyInsets(toolbar)

        // Load Home fragment by default (only on first creation, not on rotation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Switch fragments when a bottom nav tab is tapped
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_categories -> CategoriesFragment()
                R.id.nav_account -> AccountFragment()
                R.id.nav_cart -> CartFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                // Jump straight to the Account tab instead of just a toast
                findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.nav_account
                true
            }
            R.id.menu_about -> {
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_settings -> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_logout -> {
                showLogoutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                SessionManager.clearLoggedInEmail(this)
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}