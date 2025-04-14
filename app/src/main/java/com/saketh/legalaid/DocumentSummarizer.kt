package com.saketh.legalaid

import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import org.json.JSONArray
import org.json.JSONObject

class DocumentSummarizer(private val context: Context) {
    private val client = OkHttpClient()

    // Set this to true for mock responses, false for real Gemini API
    private val USE_MOCK_DATA = true

    companion object {
        // If using Android Emulator, use 10.0.2.2
        // If using real device, use your computer's IP address from ipconfig
        private const val SERVER_IP = "10.0.2.2" // or your computer's IP like "192.168.1.100"
        private const val SERVER_PORT = "5000"
        private const val SERVER_URL = "http://$SERVER_IP:$SERVER_PORT/summarize"
    }

    interface SummaryCallback {
        fun onSuccess(response: JSONObject)
        fun onError(error: String)
    }

    fun summarizeDocument(file: File, callback: SummaryCallback) {
        if (USE_MOCK_DATA) {
            // Return mock data immediately
            provideMockResponse(callback)
            return
        }

        val mediaType = "application/octet-stream".toMediaTypeOrNull()
            ?: throw IllegalArgumentException("Invalid media type")

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                file.asRequestBody(mediaType)
            )
            .build()

        val request = Request.Builder()
            .url(SERVER_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback.onError(e.message ?: "Network error occurred")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val summary = responseBody?.let { parseSummary(it) }
                    if (summary != null) {
                        callback.onSuccess(summary)
                    } else {
                        callback.onError("Failed to parse response")
                    }
                } else {
                    callback.onError("Server error: ${response.code}")
                }
            }
        })
    }

    private fun parseSummary(jsonResponse: String): JSONObject? {
        return try {
            JSONObject(jsonResponse)
        } catch (e: Exception) {
            null
        }
    }

    private fun provideMockResponse(callback: SummaryCallback) {
        // Create a mock JSON response
        val mockResponse = JSONObject().apply {
            put("title", "Confidentiality Agreement")
            put("summary", "This is a comprehensive confidentiality agreement between Company X and Party Y. " +
                    "The agreement outlines the terms and conditions for handling sensitive information shared " +
                    "between the parties during their business relationship. It includes specific provisions for " +
                    "data protection, permitted uses, and breach consequences.")

            // Add key points
            put("key_points", JSONArray().apply {
                put("All shared information must be kept strictly confidential")
                put("Agreement is valid for 5 years from signing date")
                put("Unauthorized disclosure may result in legal action")
                put("Both parties must implement security measures")
                put("Written consent required for information sharing")
            })

            // Add document type
            put("document_type", "Legal Agreement")

            // Add parties involved
            put("parties_involved", JSONArray().apply {
                put("Company X (Disclosing Party)")
                put("Party Y (Receiving Party)")
            })

            // Add important dates
            put("important_dates", JSONArray().apply {
                put("Effective from: January 1, 2024")
                put("Duration: 5 years")
                put("Review period: 30 days")
            })

            // Add action items
            put("action_items", JSONArray().apply {
                put("Sign and return within 14 days")
                put("Implement data protection measures")
                put("Create list of authorized personnel")
                put("Set up secure communication channels")
            })
        }

        // Simulate network delay (1 second)
        Thread {
            Thread.sleep(1000)
            callback.onSuccess(mockResponse)
        }.start()
    }
}