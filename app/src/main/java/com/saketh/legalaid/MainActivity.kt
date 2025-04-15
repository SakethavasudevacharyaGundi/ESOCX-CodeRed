package com.saketh.legalaid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saketh.legalaid.activities.DocumentInterpreterActivity
import com.saketh.legalaid.databinding.ActivityMainBinding
import com.saketh.legalaid.base.BaseActivity
import com.saketh.legalaid.utils.LocaleHelper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isHindi = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check current language
        isHindi = LocaleHelper.getLanguage(this) == "hi"

        setupClickListeners()
        setupBottomNavigation()
        startTitleAnimation()
        setupLanguageSwitch()
    }

    private fun startTitleAnimation() {
        binding.titleText.setCharacterDelay(150) // 150ms delay between characters
        binding.titleText.animateText(getString(R.string.app_name))
    }

    private fun setupClickListeners() {
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
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupLanguageSwitch() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                toggleLanguage()
                return true
            }
        })

        binding.titleText.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun toggleLanguage() {
        isHindi = !isHindi
        val newLocale = if (isHindi) "hi" else "en"
        
        // Update the locale
        val config = resources.configuration
        val locale = Locale(newLocale)
        Locale.setDefault(locale)
        config.setLocale(locale)
        
        // Update the context
        createConfigurationContext(config)
        
        // Save the language preference
        LocaleHelper.setLocale(this, newLocale)
        
        // Restart the activity
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}