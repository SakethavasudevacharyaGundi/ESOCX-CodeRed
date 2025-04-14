package com.saketh.legalaid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.saketh.legalaid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        setupBottomNavigation()
        startTitleAnimation()
    }

    private fun startTitleAnimation() {
        binding.titleText.setTextDirectly("Legal Aid.") // Set the text directly without animation
    }

    private fun setupClickListeners() {
        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.cardDocumentInterpreter.setOnClickListener {
            startActivity(Intent(this, DocumentInterpreterActivity::class.java))
        }

        binding.cardChatAssistant.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        binding.cardCaseTracker.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }

        binding.cardGetLegalAid.setOnClickListener {
            startActivity(Intent(this, LegalAidRequestActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already on home, do nothing or refresh
                    true
                }
                R.id.navigation_documents -> {
                    startActivity(Intent(this, DocumentInterpreterActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    Toast.makeText(this, "Profile Coming Soon", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}