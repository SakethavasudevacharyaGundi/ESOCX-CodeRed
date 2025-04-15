package com.saketh.legalaid

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ChatBot(private val context: Context) {
    private val client = OkHttpClient()

    companion object {
        private const val SERVER_IP = "10.0.2.2"  // localhost for Android Emulator
        private const val SERVER_PORT = "5000"
        private const val SERVER_URL = "http://$SERVER_IP:$SERVER_PORT/chatbot"
    }

    interface ChatCallback {
        fun onSuccess(response: String)
        fun onError(error: String)
    }

    fun sendMessage(message: String, callback: ChatCallback) {
        android.util.Log.d("ChatBot", "Sending message: $message")
        
        val jsonBody = JSONObject().apply {
            put("message", message)
        }

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(SERVER_URL)
            .post(requestBody)
            .build()

        android.util.Log.d("ChatBot", "Sending request to server: $SERVER_URL")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                android.util.Log.e("ChatBot", "Network error: ${e.message}")
                callback.onError("Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    android.util.Log.d("ChatBot", "Response body: $responseBody")
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val botResponse = jsonResponse.optString("response", "Sorry, I couldn't process that.")
                        callback.onSuccess(botResponse)
                    } catch (e: Exception) {
                        android.util.Log.e("ChatBot", "Error parsing response: ${e.message}")
                        callback.onError("Error parsing response")
                    }
                } else {
                    val errorBody = response.body?.string() ?: "Unknown error"
                    android.util.Log.e("ChatBot", "Server error: ${response.code}, Body: $errorBody")
                    callback.onError("Server error: ${response.code}\n$errorBody")
                }
            }
        })
    }
} 