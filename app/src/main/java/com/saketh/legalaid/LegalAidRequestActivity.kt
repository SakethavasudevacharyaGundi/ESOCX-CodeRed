package com.saketh.legalaid

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.saketh.legalaid.R
import com.saketh.legalaid.api.LegalAidApi
import com.saketh.legalaid.api.LegalAidRequest
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LegalAidRequestActivity : AppCompatActivity() {

    private lateinit var caseTypeInput: AutoCompleteTextView
    private lateinit var urgencyLevelInput: AutoCompleteTextView
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var contactInfoInput: TextInputEditText
    private lateinit var submitButton: MaterialButton
    private lateinit var progressIndicator: CircularProgressIndicator

    private val api: LegalAidApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")  // Use this for Android emulator
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LegalAidApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal_aid_request)

        // Initialize views
        caseTypeInput = findViewById(R.id.caseTypeInput)
        urgencyLevelInput = findViewById(R.id.urgencyLevelInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        contactInfoInput = findViewById(R.id.contactInfoInput)
        submitButton = findViewById(R.id.submitButton)
        progressIndicator = findViewById(R.id.progressIndicator)

        // Setup toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setup back button
        findViewById<View>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Setup dropdowns
        setupCaseTypeDropdown()
        setupUrgencyLevelDropdown()

        // Setup submit button
        submitButton.setOnClickListener {
            submitRequest()
        }
    }

    private fun setupCaseTypeDropdown() {
        val caseTypes = arrayOf(
            "Criminal Defense",
            "Family Law",
            "Immigration",
            "Housing",
            "Employment",
            "Consumer Rights",
            "Civil Rights",
            "Other"
        )
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, caseTypes)
        caseTypeInput.setAdapter(adapter)
    }

    private fun setupUrgencyLevelDropdown() {
        val urgencyLevels = arrayOf(
            "High - Immediate attention required",
            "Medium - Within a week",
            "Low - General consultation"
        )
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, urgencyLevels)
        urgencyLevelInput.setAdapter(adapter)
    }

    private fun submitRequest() {
        val caseType = caseTypeInput.text.toString()
        val urgencyLevel = urgencyLevelInput.text.toString()
        val description = descriptionInput.text.toString()
        val contactInfo = contactInfoInput.text.toString()

        if (caseType.isEmpty() || urgencyLevel.isEmpty() || description.isEmpty() || contactInfo.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show progress indicator
        progressIndicator.visibility = View.VISIBLE
        submitButton.isEnabled = false

        // Convert case type and urgency level to API format
        val apiCaseType = when (caseType) {
            "Criminal Defense" -> "CRIMINAL"
            "Family Law" -> "FAMILY"
            "Immigration" -> "IMMIGRATION"
            "Housing" -> "HOUSING"
            "Employment" -> "EMPLOYMENT"
            "Consumer Rights" -> "CONSUMER"
            "Civil Rights" -> "CIVIL"
            else -> "OTHER"
        }

        val apiUrgencyLevel = when {
            urgencyLevel.startsWith("High") -> "HIGH"
            urgencyLevel.startsWith("Medium") -> "MEDIUM"
            else -> "LOW"
        }

        val request = LegalAidRequest(
            case_type = apiCaseType,
            urgency_level = apiUrgencyLevel,
            description = description,
            contact_info = contactInfo
        )

        lifecycleScope.launch {
            try {
                val response = api.submitRequest(request)
                if (response.isSuccessful) {
                    Toast.makeText(this@LegalAidRequestActivity, "Request submitted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@LegalAidRequestActivity, "Error: ${errorBody ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LegalAidRequestActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                // Hide progress indicator
                progressIndicator.visibility = View.GONE
                submitButton.isEnabled = true
            }
        }
    }
} 