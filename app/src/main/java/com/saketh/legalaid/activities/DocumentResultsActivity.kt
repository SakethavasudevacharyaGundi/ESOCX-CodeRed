package com.saketh.legalaid.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.saketh.legalaid.databinding.ActivityDocumentResultsBinding

class DocumentResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocumentResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        handleIntent()
    }

    private fun setupUI() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun handleIntent() {
        intent?.let { intent ->
            val title = intent.getStringExtra("title") ?: "Document Summary"
            val summary = intent.getStringExtra("summary") ?: ""
            val keyPoints = intent.getStringArrayListExtra("keyPoints") ?: arrayListOf()

            binding.documentTitle.text = title
            binding.summary.text = buildSummaryText(summary, keyPoints)
        }
    }

    private fun buildSummaryText(summary: String, keyPoints: ArrayList<String>): String {
        val stringBuilder = StringBuilder()
        
        // Add summary
        stringBuilder.append(summary)

        // Add key points if available
        if (keyPoints.isNotEmpty()) {
            stringBuilder.append("\n\nKey Points:\n")
            keyPoints.forEachIndexed { index, point ->
                stringBuilder.append("\n• ${point.trim()}")
            }
        }

        return stringBuilder.toString()
    }

    private fun showError(message: String) {
        binding.progressIndicator.visibility = View.GONE
        binding.errorText.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
} 