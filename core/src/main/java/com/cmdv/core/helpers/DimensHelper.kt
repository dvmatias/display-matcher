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

    fun spToPx(context: Context, sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
    }

    fun pxToSp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.scaledDensity
    }
}