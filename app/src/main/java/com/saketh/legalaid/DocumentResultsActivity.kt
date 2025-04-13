package com.saketh.legalaid

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DocumentResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_results)

        // Get data from intent
        val summary = intent.getStringExtra("summary") ?: ""
        val keyPoints = intent.getStringArrayListExtra("keyPoints") ?: arrayListOf()
        val title = intent.getStringExtra("title") ?: "Document Summary"

        // Set the title
        findViewById<TextView>(R.id.documentTitle).text = title

        // Set the summary
        findViewById<TextView>(R.id.summary).text = summary

        // Add key points
        val keyPointsContainer = findViewById<LinearLayout>(R.id.keyPointsContainer)
        keyPoints.forEach { point ->
            addBulletPoint(keyPointsContainer, point)
        }
    }

    private fun addBulletPoint(container: LinearLayout, text: String) {
        val bulletPoint = TextView(this).apply {
            this.text = "• $text"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }
        container.addView(bulletPoint)
    }
}