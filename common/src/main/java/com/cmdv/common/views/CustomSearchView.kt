package com.cmdv.common.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.cmdv.common.R
import com.cmdv.common.databinding.SearchViewCustomBinding

enum class SearchViewType(val value: Int) {
    BUTTON(0),
    SEARCH(1)
}

enum class SearchViewState {
    FOR_INPUT,
    FOR_SEARCH,
    SEARCHING
}

class CustomSearchView : ConstraintLayout {
    private var binding: SearchViewCustomBinding = SearchViewCustomBinding.inflate(LayoutInflater.from(context))

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
            R.styleable.CustomSearchView,
            defStyleAttr ?: 1,
            0
        )
        setButtonType(a.getInt(R.styleable.CustomSearchView_view_type, 0))
        a.recycle()
    }

    private fun setButtonType(viewType: Int) {
        when (viewType) {
            SearchViewType.BUTTON.value -> binding.containerSearch.visibility = View.GONE
            SearchViewType.SEARCH.value -> binding.containerButton.visibility = View.GONE
        }
    }

    fun setButtonStateListener(listener: OnClickListener) {
        binding.viewBackgroundButton.setOnClickListener { listener.onClick(it) }
    }

}