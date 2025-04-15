package com.saketh.legalaid

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.saketh.legalaid.utils.LocaleHelper
import java.util.*

class LegalAidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize with default language
        LocaleHelper.onAttach(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.onAttach(this)
    }
} 