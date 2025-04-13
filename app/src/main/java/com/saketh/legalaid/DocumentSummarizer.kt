package com.saketh.legalaid

import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class DocumentSummarizer(private val context: Context) {
    private val client = OkHttpClient()

    companion object {
        // If using Android Emulator, use 10.0.2.2
        // If using real device, use your computer's IP address from ipconfig
        private const val SERVER_IP = "10.0.2.2" // or your computer's IP like "192.168.1.100"
        private const val SERVER_PORT = "5000"
        private const val SERVER_URL = "http://$SERVER_IP:$SERVER_PORT/summarize"
    }

    interface SummaryCallback {
        fun onSuccess(summary: String)
        fun onError(error: String)
    }

    fun summarizeDocument(file: File, callback: SummaryCallback) {
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

    private fun parseSummary(jsonResponse: String): String? {
        return try {
            val jsonObject = org.json.JSONObject(jsonResponse)
            jsonObject.getString("summary")
        } catch (e: Exception) {
            null
        }
    }
}