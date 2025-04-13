package com.saketh.legalaid

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.saketh.legalaid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rotationAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupClickListeners()
        setupBookAnimation()
    }

    private fun setupBookAnimation() {
        rotationAnimator = ObjectAnimator.ofFloat(binding.bookIcon, "rotationY", 0f, 360f).apply {
            duration = 3000 // 3 seconds for one complete rotation
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    private fun setupClickListeners() {
        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.cardDocumentInterpreter.setOnClickListener {
            Toast.makeText(this, "Document Interpreter Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.cardProcessVisualizer.setOnClickListener {
            Toast.makeText(this, "Process Visualizer Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.cardChatAssistant.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        binding.cardCaseTracker.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }

        binding.buttonGetLegalAid.setOnClickListener {
            Toast.makeText(this, "Legal Aid Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::rotationAnimator.isInitialized) {
            rotationAnimator.cancel()
        }
    }
}