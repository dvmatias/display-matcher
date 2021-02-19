package com.cmdv.common.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import com.cmdv.common.R
import com.cmdv.common.databinding.SearchViewCustomBinding
import com.cmdv.common.extensions.hideKeyboard
import com.cmdv.common.extensions.showKeyboard
import com.cmdv.common.listeners.SimpleTextWatcher


enum class SearchViewType(val value: Int) {
    BUTTON(0),
    SEARCH(1)
}

enum class SearchState {
    NONE,
    FOR_INPUT,
    FOR_SEARCH,
    SEARCHING
}

class CustomSearchView : ConstraintLayout {
    private var binding: SearchViewCustomBinding = SearchViewCustomBinding.inflate(LayoutInflater.from(context))
    private lateinit var viewType: SearchViewType
    private lateinit var searchState: SearchState
    private var listener: SearchViewListener? = null
    private var searchText: String = ""

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

    fun setListener(listener: SearchViewListener) {
        this.listener = listener
    }

    private fun initView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int?) {
        val a: TypedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomSearchView,
            defStyleAttr ?: 1,
            0
        )
        viewType =
            when (a.getInt(R.styleable.CustomSearchView_view_type, 0)) {
                SearchViewType.BUTTON.value -> SearchViewType.BUTTON
                SearchViewType.SEARCH.value -> SearchViewType.SEARCH
                else -> throw IllegalAccessException("You must define a vuew type for this element.")
            }
        setupViewsForButtonOrSearchMode()
        initViews()
        initSearchState()

        a.recycle()
    }

    private fun setupViewsForButtonOrSearchMode() {
        when (viewType) {
            SearchViewType.BUTTON -> binding.containerSearch.visibility = View.GONE
            SearchViewType.SEARCH -> binding.containerButton.visibility = View.GONE
        }
    }

    private fun initViews() {
        binding.imageViewBackButton.setOnClickListener {
            hideKeyboard()
            listener?.onBackButtonClick()
        }
        binding.imageViewClearSearchButton.setOnClickListener { listener?.onClearSearchButtonClick() }
        setupEditText()
    }

    private fun setupEditText() {
        binding.editTextSearch.apply {
            addTextChangedListener(object : SimpleTextWatcher() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchText = s.toString()
                    if (searchText.isEmpty()) {
                        setSearchState(SearchState.FOR_INPUT)
                    } else {
                        setSearchState(SearchState.FOR_SEARCH)
                    }
                }
            })
            setOnEditorActionListener { v, actionId, event ->
                if ((actionId and EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_SEARCH && searchText.isNotEmpty()) {
                    hideKeyboard()
                    listener?.onSearchClick(searchText)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun initSearchState() {
        when (viewType) {
            SearchViewType.SEARCH -> setSearchState(SearchState.FOR_INPUT)
            else -> setSearchState(SearchState.NONE)
        }
    }

    private fun setSearchState(searchState: SearchState) {
        this.searchState = searchState
        setupView()
    }

    private fun setupView() {
        when (searchState) {
            SearchState.NONE -> {
            }
            SearchState.FOR_INPUT -> {
                binding.imageViewClearSearchButton.visibility = View.GONE
                binding.editTextSearch.text.clear()
            }
            SearchState.FOR_SEARCH -> {
                binding.imageViewClearSearchButton.visibility = View.VISIBLE
            }
            SearchState.SEARCHING -> {
            }
        }
    }

    fun setButtonStateListener(listener: OnClickListener) {
        binding.viewBackgroundButton.setOnClickListener { listener.onClick(it) }
    }

    fun clearSearch() {
        setSearchState(SearchState.FOR_INPUT)
    }

    fun focus() {
        binding.editTextSearch.requestFocus()
        showKeyboard()
    }

    interface SearchViewListener {
        fun onBackButtonClick()
        fun onClearSearchButtonClick()
        fun onSearchClick(searchText: String)
    }

}