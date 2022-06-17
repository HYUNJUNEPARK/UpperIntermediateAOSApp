package com.june.ott

import android.content.Context
import android.util.TypedValue

class ExtensionFunction {
    companion object {
        fun Float.dpToPx(context: Context): Float =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
    }
}