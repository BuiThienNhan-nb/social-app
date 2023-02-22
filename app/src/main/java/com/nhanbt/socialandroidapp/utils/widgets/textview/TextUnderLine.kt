package com.nhanbt.socialandroidapp.utils.widgets.textview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextUnderLine(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    init {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        invalidate()
    }
}
