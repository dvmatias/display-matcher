package com.cmdv.feature.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.views.CustomSearchView
import com.cmdv.core.managers.SharePreferenceManager
import com.cmdv.domain.models.RecentSearchModel
import com.cmdv.feature.adapters.RecentSearchRecyclerAdapter
import com.cmdv.feature.databinding.ActivitySearchBinding
import com.cmdv.feature.databinding.LayoutRecentSearchBinding
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var mergeRecentSearchBinding: LayoutRecentSearchBinding
    private lateinit var recentSearchAdapter: RecentSearchRecyclerAdapter
    private val sharePreferenceManager: SharePreferenceManager = SharePreferenceManager(this)

    private val recentSearchListener = object : RecentSearchRecyclerAdapter.RecentSearchListener {
        override fun onRecentSearchClick(query: String) {
            Toast.makeText(this@SearchActivity, "Click on $query", Toast.LENGTH_SHORT).show()
        }
    }

    private val searchViewListener = object : CustomSearchView.SearchViewListener {
        override fun onBackButtonClick() {
            finish()
        }

        override fun onClearSearchButtonClick() {
            binding.customViewSearchView.clearSearch()
        }

        override fun onSearchClick(query: String) {
            // TODO search saving should be done once the search is done. Move to proper place.
            saveSearch(query)
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
        recentSearchAdapter = RecentSearchRecyclerAdapter(this, recentSearchListener)
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

    private fun saveSearch(query: String) {
        val recentSearch = RecentSearchModel(
            query,
            Date(),
            true,
            ""
        )
        sharePreferenceManager.addRecentSearch(recentSearch)
    }
}