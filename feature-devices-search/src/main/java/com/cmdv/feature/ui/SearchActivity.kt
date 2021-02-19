package com.cmdv.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cmdv.common.views.CustomSearchView
import com.cmdv.feature.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val searchViewListener = object : CustomSearchView.SearchViewListener {
        override fun onBackButtonClick() {
            finish()
        }

        override fun onClearSearchButtonClick() {
            binding.customViewSearchView.clearSearch()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customViewSearchView.apply {
            setListener(searchViewListener)
            focus()
        }
    }
}