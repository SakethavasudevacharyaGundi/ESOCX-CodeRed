package com.saketh.legalaid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.saketh.legalaid.databinding.ActivityDocumentInterpreterBinding
import java.io.File
import java.io.FileOutputStream
import org.json.JSONObject

class DocumentInterpreterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocumentInterpreterBinding
    private val documentSummarizer by lazy { DocumentSummarizer(this) }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { handleSelectedFile(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentInterpreterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.uploadCard.setOnClickListener {
            openFilePicker()
        }
    }

    private fun openFilePicker() {
        getContent.launch("*/*")  // Accept all file types
    }

    private fun handleSelectedFile(uri: Uri) {
        // Show progress indicator
        binding.progressIndicator.visibility = View.VISIBLE
        binding.summaryCard.visibility = View.GONE

        try {
            // Get file name
            val fileName = getFileName(uri)

            // Create temporary file
            val tempFile = createTempFile(uri, fileName)

            // Process the document
            documentSummarizer.summarizeDocument(tempFile, object : DocumentSummarizer.SummaryCallback {
                override fun onSuccess(response: JSONObject) {
                    runOnUiThread {
                        showDocumentResults(response)
                    }
                }

                override fun onError(error: String) {
                    runOnUiThread {
                        showError(error)
                    }
                }
            })
        } catch (e: Exception) {
            showError("Error processing file: ${e.message}")
        }
    }

    private fun getFileName(uri: Uri): String {
        var fileName = ""
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        }
        return fileName.ifEmpty { "document_${System.currentTimeMillis()}" }
    }

    private fun createTempFile(uri: Uri, fileName: String): File {
        val tempFile = File(cacheDir, fileName)
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

    private fun showSummary(summary: String) {
        binding.progressIndicator.visibility = View.GONE
        binding.summaryCard.visibility = View.VISIBLE
        binding.summaryText.text = summary
    }

    private fun showError(error: String) {
        binding.progressIndicator.visibility = View.GONE
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun showDocumentResults(response: JSONObject) {
        val intent = Intent(this, DocumentResultsActivity::class.java).apply {
            putExtra("summary", response.optString("summary", ""))
            putExtra("title", response.optString("title", "Document Summary"))
            val keyPoints = ArrayList<String>()
            val keyPointsArray = response.optJSONArray("key_points")
            if (keyPointsArray != null) {
                for (i in 0 until keyPointsArray.length()) {
                    keyPoints.add(keyPointsArray.optString(i))
                }
            }
            putStringArrayListExtra("keyPoints", keyPoints)
        }
        startActivity(intent)
    }
}