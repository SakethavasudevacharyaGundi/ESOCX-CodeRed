package com.saketh.legalaid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saketh.legalaid.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(message: ChatMessage, timeFormat: SimpleDateFormat) {
            try {
                // Show/hide layouts based on message type
                binding.userMessageLayout.visibility = if (message.isUser) View.VISIBLE else View.GONE
                binding.aiMessageLayout.visibility = if (message.isUser) View.GONE else View.VISIBLE
                
                val currentTime = timeFormat.format(Date())
                
                if (message.isUser) {
                    binding.userMessageText.text = message.text
                    binding.userMessageTime.text = currentTime
                } else {
                    binding.aiMessageText.text = message.text
                    binding.aiMessageTime.text = currentTime
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position], timeFormat)
    }

    override fun getItemCount() = messages.size
} 