package com.cmdv.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.cmdv.common.R
import com.cmdv.common.adapters.FilterType
import com.cmdv.common.databinding.FilterSelectorViewCustomBinding

enum class FilterStatus {
    NONE,
    SELECTED
}

class CustomFilterSelectorView : ConstraintLayout {
    private var binding: FilterSelectorViewCustomBinding = FilterSelectorViewCustomBinding.inflate(LayoutInflater.from(context))
    private lateinit var releaseStatusLabels: Array<out String>
    private lateinit var categoriesLabels: Array<out String>

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

    fun setupListeners(listener: OnFilterClickListener) {
        binding.textViewFilterReleaseStatus.setOnClickListener { listener.onFilterClick(FilterType.RELEASE_STATUS) }
        binding.textViewFilterCategories.setOnClickListener { listener.onFilterClick(FilterType.CATEGORY) }
    }

    private fun initView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int?) {
        releaseStatusLabels = context.resources.getStringArray(R.array.labels_filter_release_status)
        categoriesLabels = context.resources.getStringArray(R.array.labels_filter_categories)

        setReleaseStatusFilterLabel(releaseStatusLabels[0])
        setCategoryFilerLabel(categoriesLabels[0])
        setReleaseStatusFilterStatus(context)
        setCategoryFilterStatus(context, FilterStatus.NONE)
    }

    private fun setReleaseStatusFilterLabel(statusFilterLabel: String) {
        binding.textViewFilterReleaseStatus.text = statusFilterLabel
    }

    private fun setCategoryFilerLabel(categoryFilterLabel: String) {
        binding.textViewFilterCategories.text = categoryFilterLabel
    }

    private fun setReleaseStatusFilterStatus(context: Context) {
        binding.textViewFilterReleaseStatus.apply {
            background = ContextCompat.getDrawable(context, R.drawable.shape_filter_selector_background_selected)
            setTextColor(ContextCompat.getColor(context, R.color.text_label_filter_selected))
        }
    }

    private fun setCategoryFilterStatus(context: Context, filterStatus: FilterStatus) {
        val backgroundDrawable = when (filterStatus) {
            FilterStatus.NONE -> ContextCompat.getDrawable(context, R.drawable.shape_filter_selector_background_none)
            FilterStatus.SELECTED -> ContextCompat.getDrawable(context, R.drawable.shape_filter_selector_background_selected)
        }
        val textColor = when (filterStatus) {
            FilterStatus.NONE -> ContextCompat.getColor(context, R.color.text_label_filter_none)
            FilterStatus.SELECTED -> ContextCompat.getColor(context, R.color.text_label_filter_selected)
        }
        binding.textViewFilterCategories.apply {
            background = backgroundDrawable
            setTextColor(textColor)
        }
    }

    interface OnFilterClickListener {
        fun onFilterClick(filterType: FilterType)
    }
}