package com.cmdv.common.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.viewpager.widget.ViewPager
import com.cmdv.common.R
import com.cmdv.common.databinding.CustomDeviceDetailBottomNavViewBinding
import com.cmdv.common.listeners.SimplePageChangeListener
import kotlin.math.abs

private const val DEFAULT_SCALE = 1f
private const val MAX_SCALE = 15f
private const val BASE_DURATION = 300L
private const val VARIABLE_DURATION = 300L

class CustomDeviceDetailBottomNavView : ConstraintLayout {
    private var binding: CustomDeviceDetailBottomNavViewBinding =
        CustomDeviceDetailBottomNavViewBinding.inflate(LayoutInflater.from(context), this, false)
    private var animator: ValueAnimator? = null
    private var pager: ViewPager? = null

    private var selectedItemId: Int = -1

    private val indicator = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.main_400)
    }

    private val bottomOffset = resources.getDimension(R.dimen.bottom_margin)
    private val defaultSize = resources.getDimension(R.dimen.default_size)

    constructor(context: Context) : super(context) {
        initView(context, null, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context, attributeSet, null)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initView(context, attributeSet, defStyleAttr)
    }

    fun setupWithViewPager(pager: ViewPager) {
        this.pager = pager
        this.pager?.addOnPageChangeListener(object : SimplePageChangeListener() {
            override fun onPageSelected(position: Int) {
                val tag =
                    when (position) {
                        0 -> "general"
                        1 -> "hardware"
                        2 -> "multimedia"
                        3 -> "connectivity"
                        else -> "general"
                    }
                val itemId = findViewWithTag<View>(tag).id
                onItemSelected(itemId, true)
            }
        })
    }

    init {
        selectedItemId =  binding.imageViewGeneral.id
        addView(binding.root)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int?) {
        binding.imageViewGeneral.setOnClickListener { onItemSelected(it.id, true) }
        binding.imageViewHardware.setOnClickListener { onItemSelected(it.id, true) }
        binding.imageViewMultimedia.setOnClickListener { onItemSelected(it.id, true) }
        binding.imageViewConnectivity.setOnClickListener { onItemSelected(it.id, true) }
        binding.textViewGeneral.setOnClickListener { onItemSelected(it.id, true) }
        binding.textViewHardware.setOnClickListener { onItemSelected(it.id, true) }
        binding.textViewMultimedia.setOnClickListener { onItemSelected(it.id, true) }
        binding.textViewConnectivity.setOnClickListener { onItemSelected(it.id, true) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doOnPreDraw {
            // Move the indicator in place when the view is laid out
            onItemSelected(selectedItemId, false)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clean up the animator if the view is going away
        cancelAnimator(setEndValues = true)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isLaidOut) {
            val cornerRadius = indicator.height() / 2f
            canvas.drawRoundRect(indicator, cornerRadius, cornerRadius, paint)
        }
    }

    private fun onItemSelected(itemId: Int, animate: Boolean = true) {
        if (!isLaidOut) return

        // Interrupt any current animation, but don't set the end values,
        // if it's in the middle of a movement we want it to start from
        // the current position, to make the transition smoother.
        cancelAnimator(setEndValues = false)

        val itemView = findViewById<View>(itemId) ?: return
        val fromCenterX = indicator.centerX()
        val fromScale = indicator.width() / defaultSize

        selectPage(itemView.tag.toString())

        animator = ValueAnimator.ofFloat(fromScale, MAX_SCALE, DEFAULT_SCALE).apply {
            addUpdateListener {
                val progress = it.animatedFraction
                val distanceTravelled = linearInterpolation(progress, fromCenterX, itemView.x + itemView.width / 2)

                val scale = it.animatedValue as Float
                val indicatorWidth = defaultSize * scale

                val left = distanceTravelled - indicatorWidth / 2f
                val top = height - bottomOffset - defaultSize
                val right = distanceTravelled + indicatorWidth / 2f
                val bottom = height - bottomOffset

                indicator.set(left, top, right, bottom)
                invalidate()
            }

            interpolator = LinearOutSlowInInterpolator()

            val distanceToMove = abs(fromCenterX - itemView.x + itemView.width / 2)
            duration = if (animate) calculateDuration(distanceToMove) else 0L

            start()
        }
    }

    private fun selectPage(tag: String?) {
        tag?.let {
            pager?.setCurrentItem(
                when(tag) {
                    "general" -> 0
                    "hardware" -> 1
                    "multimedia" -> 2
                    "connectivity" -> 3
                    else -> 0
                }
            )
        }
    }

    private fun calculateDuration(distance: Float) =
        (BASE_DURATION + VARIABLE_DURATION * (distance / width).coerceIn(0f, 1f)).toLong()

    private fun linearInterpolation(t: Float, a: Float, b: Float) = (1 - t) * a + t * b

    private fun cancelAnimator(setEndValues: Boolean) = animator?.let {
        if (setEndValues) {
            it.end()
        } else {
            it.cancel()
        }
        it.removeAllUpdateListeners()
        animator = null
    }

}