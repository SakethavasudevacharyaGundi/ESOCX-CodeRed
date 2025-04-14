package com.saketh.legalaid.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TypeWriter : AppCompatTextView {
    private var text: CharSequence? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    // Method to set text directly without animation
    fun setTextDirectly(txt: CharSequence) {
        text = txt
        super.setText(text, BufferType.NORMAL) // Set the text directly without animation
    }
} 