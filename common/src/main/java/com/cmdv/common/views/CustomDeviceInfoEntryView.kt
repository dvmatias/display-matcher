package com.cmdv.common.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.cmdv.common.R
import com.cmdv.common.databinding.CustomDeviceInfoEntryViewBinding


enum class ViewType(val value: Int) {
    SIMPLE(0),
    MULTIPLE(1),
    BOOLEAN(2)
}

class CustomDeviceInfoEntryView : ConstraintLayout {
    private var binding: CustomDeviceInfoEntryViewBinding =
        CustomDeviceInfoEntryViewBinding.inflate(LayoutInflater.from(context), this, false)
    private lateinit var viewType: ViewType

    constructor(context: Context) : super(context) {
        initView(context, null, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context, attributeSet, null)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initView(context, attributeSet, defStyleAttr)
    }

    init {
        addView(binding.root)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int?) {
        val a: TypedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomDeviceInfoEntryView,
            defStyleAttr ?: 1,
            0
        )
        viewType =
            when (a.getInt(R.styleable.CustomDeviceInfoEntryView_device_info_entry_view_type, -1)) {
                ViewType.SIMPLE.value -> ViewType.SIMPLE
                ViewType.MULTIPLE.value -> ViewType.MULTIPLE
                ViewType.BOOLEAN.value -> ViewType.BOOLEAN
                else -> throw IllegalAccessException("You must define a view type for this element.")
            }
        initViewType()
        a.recycle()
    }

    private fun initViewType() {
        fun setSimpleViewType() {
            binding.simpleContainer.visibility = View.VISIBLE
        }

        fun setBooleanViewType() {
            binding.booleanContainer.visibility = View.VISIBLE
        }


        when (viewType) {
            ViewType.SIMPLE -> setSimpleViewType()
            ViewType.MULTIPLE -> binding.simpleContainer.visibility = View.VISIBLE
            ViewType.BOOLEAN -> setBooleanViewType()
        }
    }

    fun setInfo(label: String, info: String) {
        when (viewType) {
            ViewType.SIMPLE -> {
                binding.textViewSimpleLabel.text = label
                binding.textViewSimpleInfo.text = info
            }
            ViewType.MULTIPLE -> {
            }
            ViewType.BOOLEAN -> {
                binding.textViewBooleanLabel.text = label
            }
        }

    }

    fun setInfo(label: Int, info: String?) {
        setInfo(context.getString(label), info ?: "")
    }

    fun setInfo(label: Int, state: Boolean, info: String? = null) {
        setInfo(label, info)

        if (state) {
            binding.booleanContainer.background =
                ContextCompat.getDrawable(context, R.drawable.shape_custom_device_info_entry_view_boolean_positive_bgr)
            binding.textViewBooleanInfo.text = context.getString(R.string.text_device_info_entry_view_yes)
        } else {
            binding.booleanContainer.background =
                ContextCompat.getDrawable(context, R.drawable.shape_custom_device_info_entry_view_boolean_negative_bgr)
            binding.textViewBooleanInfo.text = context.getString(R.string.text_device_info_entry_view_no)
        }
    }

    fun setInfo(label: Int, info: List<String>) {

    }

    fun setInfo(label: String, info: List<String>) {

    }
}