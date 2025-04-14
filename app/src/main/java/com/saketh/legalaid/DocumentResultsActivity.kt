package com.saketh.legalaid

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator

class DocumentResultsActivity : AppCompatActivity() {
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_results)

        // Initialize views
        progressIndicator = findViewById(R.id.progressIndicator)
        errorText = findViewById(R.id.errorText)

        // Show loading state
        showLoading()

        // Get data from intent
        val summary = intent.getStringExtra("summary") ?: ""
        val title = intent.getStringExtra("title") ?: "Document Summary"

        if (summary.isEmpty()) {
            showError("No content available")
            return
        }

        // Set the title
        findViewById<TextView>(R.id.documentTitle).text = title

        // Set the summary
        findViewById<TextView>(R.id.summary).text = summary

        // Hide loading state
        hideLoading()
    }

    private fun showLoading() {
        progressIndicator.visibility = View.VISIBLE
        errorText.visibility = View.GONE
    }

    private fun hideLoading() {
        progressIndicator.visibility = View.GONE
    }

    private fun showError(message: String) {
        progressIndicator.visibility = View.GONE
        errorText.visibility = View.VISIBLE
        errorText.text = message
    }
}