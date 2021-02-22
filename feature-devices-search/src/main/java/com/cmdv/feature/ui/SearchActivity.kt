package com.cmdv.feature.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.utils.Constants
import com.cmdv.common.views.CustomSearchView
import com.cmdv.core.managers.SharePreferenceManager
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.RecentSearchModel
import com.cmdv.feature.adapters.RecentSearchRecyclerAdapter
import com.cmdv.feature.adapters.SuggestionSearchRecyclerAdapter
import com.cmdv.feature.databinding.ActivitySearchBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

enum class SearchActivityViewState {
    RECENT_SEARCHES,
    SUGGESTION_SEARCHES,
    SEARCHING
}

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var recentSearchAdapter: RecentSearchRecyclerAdapter

    private lateinit var suggestionSearchAdapter: SuggestionSearchRecyclerAdapter
    private val sharePreferenceManager: SharePreferenceManager = SharePreferenceManager(this)
    private lateinit var manufacturerId: String
    private var isShowingSuggestionSearches: Boolean = false

    /**
     * Interface to catch "recent searches" events.
     */
    private val recentSearchListener = object : RecentSearchRecyclerAdapter.RecentSearchListener {
        override fun onRecentSearchClick(recentSearchTerm: String) {
            binding.customViewSearchView.apply {
                setSearchTerm(recentSearchTerm)
                performSearch(recentSearchTerm)
            }
        }
    }

    /**
     * Interface to catch "suggestion searches" events.
     */
    private val suggestionSearchListener = object : SuggestionSearchRecyclerAdapter.SuggestionListener {
        override fun onSuggestionSearchClick(suggestionsSearchTerm: String) {
            binding.customViewSearchView.apply {
                setSearchTerm(suggestionsSearchTerm)
                performSearch(suggestionsSearchTerm)
            }
        }
    }

    /**
     * Interface to catch [CustomSearchView] events.
     */
    private val searchViewListener = object : CustomSearchView.SearchViewListener {
        override fun onQueryChanged(searchTerm: String) {
            if (searchTerm.isNotEmpty()) {
                setViewState(SearchActivityViewState.SUGGESTION_SEARCHES)
                val similarRecentSearches = sharePreferenceManager.findRecentSearchesFromQueryOrderedByNewestToOldest(searchTerm)
                setRecentSearches(similarRecentSearches)
                getSuggestions(searchTerm)
            } else {
                setViewState(SearchActivityViewState.RECENT_SEARCHES)
                setRecentSearches(sharePreferenceManager.getAllRecentSearches())
                setSuggestions(null)
            }
        }

        override fun onBackButtonClick() {
            finish()
        }

        override fun onClearSearchButtonClick() {
            binding.customViewSearchView.clearSearch()
        }

        override fun onSearchClick(searchTerm: String) {
            saveSearch(searchTerm)
            searchDevices(searchTerm)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtras()
        initViews()
        setupSearchView()
        setupRecentSearchRecyclerView()
        setupSuggestionRecyclerView()
    }

    private fun getExtras() {
        manufacturerId = intent.extras?.getString(Constants.EXTRA_MANUFACTURER_ID_KEY, "") ?: ""
    }

    private fun initViews() {
        setViewState(SearchActivityViewState.RECENT_SEARCHES)
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
        setRecentSearches(sharePreferenceManager.getAllRecentSearches())
    }

    private fun setupSuggestionRecyclerView() {
        suggestionSearchAdapter = SuggestionSearchRecyclerAdapter(suggestionSearchListener)
        binding.layoutSuggestionSearches.recyclerViewSuggestionSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = suggestionSearchAdapter
        }
    }

    private fun getSuggestions(searchTerm: String) {
        viewModel.devicesFoundedByNameLiveData.observe(this, {
            it.data?.let { suggestionsSearches -> setSuggestions(suggestionsSearches) }
        })
        viewModel.searchDevicesByName(searchTerm, manufacturerId)
    }

    private fun searchDevices(searchTerm: String) {
        viewModel.devicesFoundedByNameLiveData.observe(this, {
            it.data?.let { foundedDevices -> setFoundedDevices(foundedDevices) }
        })
        viewModel.searchDevicesByName(searchTerm, manufacturerId)
    }

    private fun setRecentSearches(recentSearches: List<RecentSearchModel>) {
        if (recentSearches.isEmpty()) {
            recentSearchAdapter.setItems(listOf(), isShowingSuggestionSearches)
        } else {
            recentSearchAdapter.setItems(recentSearches, isShowingSuggestionSearches)
        }
    }

    private fun setSuggestions(suggestions: List<DeviceModel>?) {
        if (suggestions == null || suggestions.isEmpty()) {
            suggestionSearchAdapter.setItems(listOf())
        } else {
            suggestionSearchAdapter.setItems(suggestions)
        }
    }

    private fun setFoundedDevices(devices: List<DeviceModel>) {
        Log.d("Shit!", "asdsa")
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

    private fun setViewState(state: SearchActivityViewState) {
        fun showRecentSearchesView(show: Boolean) {
            binding.layoutRecentSearches.containerRecentSearch.visibility = if (show) View.VISIBLE else View.GONE
        }

        fun showSuggestionView(show: Boolean) {
            binding.layoutSuggestionSearches.containerSuggestion.visibility = if (show) View.VISIBLE else View.GONE
            isShowingSuggestionSearches = show
        }
        when (state) {
            SearchActivityViewState.RECENT_SEARCHES -> {
                showRecentSearchesView(true)
                showSuggestionView(false)
            }
            SearchActivityViewState.SUGGESTION_SEARCHES -> {
                showRecentSearchesView(true)
                showSuggestionView(true)
            }
            SearchActivityViewState.SEARCHING -> {
                showRecentSearchesView(false)
                showSuggestionView(false)
            }

        }
    }
}