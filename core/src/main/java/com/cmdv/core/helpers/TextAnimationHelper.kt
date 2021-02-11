package com.cmdv.core.helpers

import android.animation.ValueAnimator
import android.content.Context
import android.widget.TextView

object TextAnimationHelper {

    fun animateTextSize(context: Context, startSize: Float, endSize: Float, dur: Long, vararg views: TextView) = kotlin.run {
        ValueAnimator.ofFloat(startSize, endSize).apply {
            duration = dur
            addUpdateListener {
                val animatedValue: Float = it.animatedValue as Float
                DimensHelper.pxToSp(context, animatedValue).let { textSize ->
                    views.forEach { view ->
                        view.textSize = textSize
                    }
                }
            }
        }.start()
    }

}