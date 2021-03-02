package com.cmdv.common.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
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
            when (a.getInt(R.styleable.CustomDeviceInfoEntryView_device_info_entry_view_type, 0)) {
                ViewType.SIMPLE.value -> ViewType.SIMPLE
                ViewType.MULTIPLE.value -> ViewType.MULTIPLE
                ViewType.BOOLEAN.value -> ViewType.BOOLEAN
                else -> throw IllegalAccessException("You must define a view type for this element.")
            }
        a.recycle()
    }

    fun setInfo(label: String, info: String) {
        binding.textViewSimpleLabel.text = label
        binding.textViewSimpleInfo.text = info
    }

    fun setInfo(label: Int, info: String) {
        setInfo(context.getString(label), info)
    }

    fun setInfo(label: String, state: Boolean) {

    }

    fun setInfo(label: String, info: List<String>) {

    }
}