package com.saketh.legalaid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            messages.add(ChatMessage(messageText, true))
            adapter.notifyItemInserted(messages.size - 1)
            binding.chatRecyclerView.scrollToPosition(messages.size - 1)
            binding.messageInput.text.clear()
            
            // Simulate bot response (you can replace this with actual API call)
            Handler(Looper.getMainLooper()).postDelayed({
                val botResponse = "This is a sample response to: $messageText"
                messages.add(ChatMessage(botResponse, false))
                adapter.notifyItemInserted(messages.size - 1)
                binding.chatRecyclerView.scrollToPosition(messages.size - 1)
            }, 1000)
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