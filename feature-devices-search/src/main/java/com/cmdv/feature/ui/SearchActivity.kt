package com.cmdv.feature.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.views.CustomSearchView
import com.cmdv.core.managers.SharePreferenceManager
import com.cmdv.feature.adapters.RecentSearchRecyclerAdapter
import com.cmdv.feature.databinding.ActivitySearchBinding
import com.cmdv.feature.databinding.LayoutRecentSearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mergeRecentSearchBinding: LayoutRecentSearchBinding
    private val recentSearchAdapter = RecentSearchRecyclerAdapter(this)
    private val sharePreferenceManager: SharePreferenceManager = SharePreferenceManager(this)

    private val searchViewListener = object : CustomSearchView.SearchViewListener {
        override fun onBackButtonClick() {
            finish()
        }

        override fun onClearSearchButtonClick() {
            binding.customViewSearchView.clearSearch()
        }

        override fun onSearchClick(searchText: String) {
            sharePreferenceManager.addRecentSearch(searchText)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        mergeRecentSearchBinding = LayoutRecentSearchBinding.bind(binding.root)
        setContentView(binding.root)

        setupSearchView()
        setupRecentSearchRecyclerView()
    }

    private fun setupSearchView() {
        binding.customViewSearchView.apply {
            setListener(searchViewListener)
            focus()
        }
    }

    private fun setupRecentSearchRecyclerView() {
        mergeRecentSearchBinding.recyclerViewRecentSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recentSearchAdapter
        }
        showRecentSearch()
    }

    private fun showRecentSearch() {
        val recentSearch = sharePreferenceManager.getRecentSearch()
        recentSearchAdapter.setItems(recentSearch)
    }
}