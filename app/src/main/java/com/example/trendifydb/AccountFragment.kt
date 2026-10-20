package com.example.trendifydb

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import java.util.Calendar

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var ivProfilePic: ImageView
    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etContact: EditText
    private lateinit var etDob: EditText
    private lateinit var db: TrendifyDatabaseHelper
    private var loggedInEmail: String? = null

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            ivProfilePic.setImageBitmap(bitmap)
            ivProfilePic.setPadding(0, 0, 0, 0)
        } else {
            Toast.makeText(requireContext(), "No picture captured", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = TrendifyDatabaseHelper(requireContext())
        loggedInEmail = SessionManager.getLoggedInEmail(requireContext())

        ivProfilePic = view.findViewById(R.id.ivProfilePic)
        etName = view.findViewById(R.id.etName)
        etAddress = view.findViewById(R.id.etAddress)
        etEmail = view.findViewById(R.id.etEmail)
        etContact = view.findViewById(R.id.etContact)
        etDob = view.findViewById(R.id.etDob)

        val btnTakePicture = view.findViewById<Button>(R.id.btnTakePicture)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        ivProfilePic.setOnClickListener { takePictureLauncher.launch(null) }
        btnTakePicture.setOnClickListener { takePictureLauncher.launch(null) }

        etDob.setOnClickListener { showDatePicker() }

        loadAccountDetails()

        btnSave.setOnClickListener {
            if (validateInputs()) {
                saveAccountDetails()
            }
        }
    }

    // Fetches the logged-in user's record and fills the form
    private fun loadAccountDetails() {
        val email = loggedInEmail
        if (email == null) {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show()
            return
        }
        val user = db.getUserByEmail(email)
        if (user != null) {
            etName.setText(user.name)
            etEmail.setText(user.email)
            etAddress.setText(user.address)
            etContact.setText(user.contact)
            etDob.setText(user.dob)
        }
        // Email is the account's fixed identity — don't let it be edited here
        etEmail.isEnabled = false
    }

    private fun saveAccountDetails() {
        val email = loggedInEmail
        if (email == null) {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show()
            return
        }
        val success = db.updateProfile(
            email = email,
            name = etName.text.toString().trim(),
            address = etAddress.text.toString().trim(),
            contact = etContact.text.toString().trim(),
            dob = etDob.text.toString().trim()
        )
        if (success) {
            Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Could not save profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                etDob.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.maxDate = System.currentTimeMillis() - 1
        dialog.show()
    }

    private fun validateInputs(): Boolean {
        if (etName.text.toString().trim().isEmpty()) {
            etName.error = "Name is required"
            return false
        }
        if (etAddress.text.toString().trim().isEmpty()) {
            etAddress.error = "Address is required"
            return false
        }
        val email = etEmail.text.toString().trim()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email"
            return false
        }
        val contact = etContact.text.toString().trim()
        if (contact.length != 10) {
            etContact.error = "Enter a valid 10-digit contact number"
            return false
        }
        if (etDob.text.toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Please select your date of birth", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}