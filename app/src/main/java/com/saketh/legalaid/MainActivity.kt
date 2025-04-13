package com.saketh.legalaid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saketh.legalaid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardDocumentInterpreter.setOnClickListener {
            Toast.makeText(this, "Document Interpreter Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.cardProcessVisualizer.setOnClickListener {
            Toast.makeText(this, "Process Visualizer Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.cardChatAssistant.setOnClickListener {
            Toast.makeText(this, "AI Chat Assistant Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.cardCaseTracker.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }

        binding.buttonGetLegalAid.setOnClickListener {
            Toast.makeText(this, "Legal Aid Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }
}