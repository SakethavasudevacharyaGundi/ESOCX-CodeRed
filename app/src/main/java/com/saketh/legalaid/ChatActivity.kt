package com.saketh.legalaid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saketh.legalaid.databinding.ActivityChatBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var chatBot: ChatBot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatBot = ChatBot(this)
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        try {
            adapter = ChatAdapter(messages)
            binding.chatRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = this@ChatActivity.adapter
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendMessage() {
        val messageText = binding.messageInput.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // Add user message
            messages.add(ChatMessage(messageText, true))
            adapter.notifyItemInserted(messages.size - 1)
            binding.chatRecyclerView.scrollToPosition(messages.size - 1)
            binding.messageInput.text.clear()
            
            // Show typing indicator
            binding.typingIndicator.visibility = View.VISIBLE

            // Get bot response
            chatBot.sendMessage(messageText, object : ChatBot.ChatCallback {
                override fun onSuccess(response: String) {
                    runOnUiThread {
                        // Hide typing indicator
                        binding.typingIndicator.visibility = View.GONE
                        
                        // Add bot response
                        messages.add(ChatMessage(response, false))
                        adapter.notifyItemInserted(messages.size - 1)
                        binding.chatRecyclerView.scrollToPosition(messages.size - 1)
                    }
                }

                override fun onError(error: String) {
                    runOnUiThread {
                        // Hide typing indicator
                        binding.typingIndicator.visibility = View.GONE
                        
                        // Show error message
                        Toast.makeText(this@ChatActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    private fun setupClickListeners() {
        // Back button clicks
        binding.backButton.setOnClickListener { finish() }

        // Set up prompt button click listeners
        binding.ipcButton.setOnClickListener {
            val promptText = binding.ipcButton.text.toString()
            binding.messageInput.setText(promptText)
            sendMessage()
        }

        binding.housingButton.setOnClickListener {
            val promptText = binding.housingButton.text.toString()
            binding.messageInput.setText(promptText)
            sendMessage()
        }

        binding.faqButton.setOnClickListener {
            val promptText = binding.faqButton.text.toString()
            binding.messageInput.setText(promptText)
            sendMessage()
        }

        // Set up send button click listener
        binding.sendButton.setOnClickListener {
            sendMessage()
        }
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean
) 