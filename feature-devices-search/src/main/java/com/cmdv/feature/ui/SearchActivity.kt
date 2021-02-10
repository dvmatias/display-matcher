package com.cmdv.feature.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.View
import com.cmdv.common.listeners.SimpleTextWatcher
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    enum class SearchViewState {
        FOR_INPUT,
        FOR_SEARCH,
        SEARCHING
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSearchViewState(SearchViewState.FOR_INPUT)
        binding.imageViewBackButton.setOnClickListener { finish() }
        binding.imageViewClearSearchButton.setOnClickListener { binding.editTextSearch.text.clear() }
        binding.editTextSearch.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.isEmpty()) {
                    setSearchViewState(SearchViewState.FOR_INPUT)
                } else {
                    setSearchViewState(SearchViewState.FOR_SEARCH)
                }
            }
        })
    }

    private fun setSearchViewState(viewState: SearchViewState) {
        fun setForInputState() {
            binding.imageViewClearSearchButton.visibility = View.GONE
        }

        fun setForSearchState() {
            binding.imageViewClearSearchButton.visibility = View.VISIBLE
        }

        fun setSearchingState() {

        }

        when (viewState) {
            SearchViewState.FOR_INPUT -> setForInputState()
            SearchViewState.FOR_SEARCH -> setForSearchState()
            SearchViewState.SEARCHING -> setSearchingState()
        }
    }
}