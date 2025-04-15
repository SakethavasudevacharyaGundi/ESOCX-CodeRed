package com.saketh.legalaid.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TypeWriter : AppCompatTextView {
    private var text: CharSequence? = null
    private var index = 0
    private var delay: Long = 150 // Default delay in milliseconds
    private val handler = Handler(Looper.getMainLooper())

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private val characterAdder = object : Runnable {
        override fun run() {
            text?.let { text ->
                if (index <= text.length) {
                    setText(text.subSequence(0, index++))
                    handler.postDelayed(this, delay)
                }
            }
        }
    }

    // Method to animate text
    fun animateText(txt: CharSequence) {
        text = txt
        index = 0
        setText("")
        handler.removeCallbacks(characterAdder)
        handler.postDelayed(characterAdder, delay)
    }

    // Method to set text directly without animation
    fun setTextDirectly(txt: CharSequence) {
        text = txt
        setText(text)
    }

    // Method to set the delay between characters
    fun setCharacterDelay(millis: Long) {
        delay = millis
    }
} 