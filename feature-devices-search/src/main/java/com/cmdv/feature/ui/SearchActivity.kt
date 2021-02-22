package com.cmdv.feature.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.utils.Constants
import com.cmdv.common.views.CustomSearchView
import com.cmdv.core.managers.SharePreferenceManager
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.RecentSearchModel
import com.cmdv.feature.adapters.DevicesRecyclerAdapter
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
    private lateinit var devicesAdapter: DevicesRecyclerAdapter
    private val sharePreferenceManager: SharePreferenceManager = SharePreferenceManager(this)
    private lateinit var manufacturerId: String

    private var recentSearches: List<RecentSearchModel> = listOf()
    private var suggestionSearches: List<DeviceModel> = listOf()
    private var devices: List<DeviceModel> = listOf()

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
     * Interface to catch "devices" events.
     */
    private val deviceListener = object : DevicesRecyclerAdapter.DeviceListener {
        override fun onDeviceClick(deviceId: String) {
            Toast.makeText(this@SearchActivity, "Device with ID: $deviceId", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Interface to catch [CustomSearchView] events.
     */
    private val searchViewListener = object : CustomSearchView.SearchViewListener {
        override fun onQueryChanged(searchTerm: String) {
            if (searchTerm.isNotEmpty()) {
                val similarRecentSearches = sharePreferenceManager.findRecentSearchesFromQueryOrderedByNewestToOldest(searchTerm)
                setRecentSearches(similarRecentSearches)
                getSuggestions(searchTerm)
            } else {
                setRecentSearches(sharePreferenceManager.getAllRecentSearches())
                setSuggestions(listOf())
                setViewState(SearchActivityViewState.RECENT_SEARCHES)
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
        setupSearchView()
        setupRecentSearchRecyclerView()
        setupSuggestionRecyclerView()
        setupDevicesRecyclerView()
        initViews()
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
        recentSearches = sharePreferenceManager.getAllRecentSearches()
    }

    private fun setupSuggestionRecyclerView() {
        suggestionSearchAdapter = SuggestionSearchRecyclerAdapter(suggestionSearchListener)
        binding.layoutSuggestionSearches.recyclerViewSuggestionSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = suggestionSearchAdapter
        }
    }

    private fun setupDevicesRecyclerView() {
        devicesAdapter = DevicesRecyclerAdapter(this, deviceListener)
        binding.layoutDevices.recyclerDevices.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = devicesAdapter
        }
    }

    private fun getSuggestions(searchTerm: String) {
        viewModel.devicesFoundedByNameLiveData.observe(this, {
            it.data?.let { suggestionsSearches ->
                setSuggestions(suggestionsSearches)
                setViewState(SearchActivityViewState.SUGGESTION_SEARCHES)
            }
        })
        viewModel.searchDevicesByName(searchTerm, manufacturerId)
    }

    private fun searchDevices(searchTerm: String) {
        viewModel.devicesFoundedByNameLiveData.observe(this, {
            it.data?.let { foundedDevices ->
                setFoundedDevices(foundedDevices)
                setViewState(SearchActivityViewState.SEARCHING)
            }
        })
        viewModel.searchDevicesByName(searchTerm, manufacturerId)
    }

    private fun setRecentSearches(recentSearches: List<RecentSearchModel>) {
        this.recentSearches = recentSearches
    }

    private fun setSuggestions(suggestions: List<DeviceModel>) {
        this.suggestionSearches = suggestions
    }

    private fun setFoundedDevices(devices: List<DeviceModel>) {
        this.devices = devices
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
            if (show && !recentSearches.isNullOrEmpty()) {
                binding.layoutRecentSearches.containerRecentSearch.visibility = View.VISIBLE
                recentSearchAdapter.setItems(recentSearches, state)
            } else {
                binding.layoutRecentSearches.containerRecentSearch.visibility = View.GONE
                recentSearchAdapter.setItems(listOf(), state)
            }
        }

        fun showSuggestionView(show: Boolean) {
            if (show && !suggestionSearches.isNullOrEmpty()) {
                binding.layoutSuggestionSearches.containerSuggestion.visibility = View.VISIBLE
                suggestionSearchAdapter.setItems(suggestionSearches)
            } else {
                binding.layoutSuggestionSearches.containerSuggestion.visibility = View.GONE
                suggestionSearchAdapter.setItems(listOf())
            }
        }

        fun showDevicesView(show: Boolean) {
            if (show && !devices.isNullOrEmpty()) {
                binding.layoutDevices.containerDevices.visibility = View.VISIBLE
            } else {
                binding.layoutDevices.containerDevices.visibility = View.GONE
            }
        }

        when (state) {
            SearchActivityViewState.RECENT_SEARCHES -> {
                showRecentSearchesView(true)
                showSuggestionView(false)
                showDevicesView(false)
            }
            SearchActivityViewState.SUGGESTION_SEARCHES -> {
                showRecentSearchesView(true)
                showSuggestionView(true)
                showDevicesView(false)
            }
            SearchActivityViewState.SEARCHING -> {
                showRecentSearchesView(false)
                showSuggestionView(false)
                showDevicesView(true)
            }

        }
    }
}