package com.cmdv.core.helpers

import android.content.Context
import android.util.TypedValue

object DimensHelper {
    fun dpToPx(context: Context, dp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

    fun pxToDp(context: Context, px: Int): Int {
        val r = context.resources
        return Math.round(px / (r.displayMetrics.densityDpi / 160f))
    }

    fun pxToActualDp(context: Context, px: Int): Float {
        val r = context.resources
        return px / (r.displayMetrics.densityDpi / 160f)
    }
}