package com.cmdv.feature.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.views.CustomSearchView
import com.cmdv.core.managers.SharePreferenceManager
import com.cmdv.domain.models.RecentSearchModel
import com.cmdv.feature.adapters.RecentSearchRecyclerAdapter
import com.cmdv.feature.databinding.ActivitySearchBinding
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var recentSearchAdapter: RecentSearchRecyclerAdapter
    private val sharePreferenceManager: SharePreferenceManager = SharePreferenceManager(this)

    /**
     * Interface to catch "recent searches" events.
     */
    private val recentSearchListener = object : RecentSearchRecyclerAdapter.RecentSearchListener {
        override fun onRecentSearchClick(query: String) {
            Toast.makeText(this@SearchActivity, "Click on $query", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Interface to catch [CustomSearchView] events.
     */
    private val searchViewListener = object : CustomSearchView.SearchViewListener {
        override fun onQueryChanged(query: String) {
            if (query.isNotEmpty()) {
                val similarRecentSearches = sharePreferenceManager.findRecentSearchesFromQueryOrderedByNewestToOldest(query)
                setRecentSearches(similarRecentSearches, true)
            } else {
                setRecentSearches(sharePreferenceManager.getAllRecentSearches(), false)
            }
        }

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
        setContentView(binding.root)

        initViews()
        setupSearchView()
        setupRecentSearchRecyclerView()
    }

    private fun initViews() {
        showRecentSearches(true)
        binding.layoutSuggestions.containerSuggestion.visibility = View.GONE
    }

    private fun setupSearchView() {
        binding.customViewSearchView.apply {
            setListener(searchViewListener)
            focus()
        }
    }

    private fun setupRecentSearchRecyclerView() {
        recentSearchAdapter = RecentSearchRecyclerAdapter(recentSearchListener)
        binding.layoutRecentSearches.recyclerViewRecentSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recentSearchAdapter
        }
        setRecentSearches(sharePreferenceManager.getAllRecentSearches(), false)
    }

    private fun setRecentSearches(recentSearches: List<RecentSearchModel>, isSimilarRecentSearches: Boolean) {
        if (recentSearches.isEmpty()) {
            showRecentSearches(false)
        } else {
            showRecentSearches(true)
            recentSearchAdapter.setItems(recentSearches, isSimilarRecentSearches)
        }
    }

    private fun showRecentSearches(show: Boolean) {
        binding.layoutRecentSearches.containerRecentSearch.visibility = if (show) View.VISIBLE else View.GONE
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