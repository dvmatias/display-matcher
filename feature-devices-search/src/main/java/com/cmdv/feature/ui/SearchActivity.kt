package com.cmdv.feature.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.utils.Constants
import com.cmdv.common.views.CustomSearchView
import com.cmdv.core.helpers.StringHelper
import com.cmdv.core.managers.SharePreferenceManager
import com.cmdv.core.navigatior.Navigator
import com.cmdv.domain.models.RecentSearchModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature.adapters.DeviceRecyclerAdapter
import com.cmdv.feature.adapters.RecentAndSuggestedSearchRecyclerAdapter
import com.cmdv.feature.databinding.ActivitySearchBinding
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var binding: ActivitySearchBinding
    private val navigator: Navigator by inject()

    private var searchTerm = ""
    private lateinit var recentAndSuggestedSearchAdapter: RecentAndSuggestedSearchRecyclerAdapter
    private lateinit var deviceAdapter: DeviceRecyclerAdapter
    private val sharePreferenceManager: SharePreferenceManager = SharePreferenceManager(this)
    private lateinit var manufacturerId: String

    /**
     * Interface to catch event over recent searches and suggested searches.
     */
    private val recentAndSuggestionListener = object : RecentAndSuggestedSearchRecyclerAdapter.RecentAndSuggestionListener {
        override fun onRecentSearchClick(recentSearchTerm: String) {
            binding.customViewSearchView.apply {
                setSearchTerm(recentSearchTerm)
                performSearch()
            }
        }

        override fun onSuggestionSearchClick(suggestionsSearchTerm: String) {
            binding.customViewSearchView.apply {
                setSearchTerm(suggestionsSearchTerm)
                performSearch()
            }
        }
    }

    /**
     * Interface to catch "devices" events.
     */
    private val deviceListener = object : DeviceRecyclerAdapter.DeviceListener {
        override fun onDeviceClick(deviceId: String) {
            val bundle = bundleOf(
                Constants.EXTRA_DEVICE_ID_KEY to deviceId,
                Constants.EXTRA_MANUFACTURER_ID_KEY to manufacturerId
            )
            navigator.toDeviceDetailsActivity(
                origin = this@SearchActivity,
                bundle = bundle,
                options = null,
                finish = true
            )
        }
    }

    /**
     * Interface to catch [CustomSearchView] events.
     */
    private val searchViewListener = object : CustomSearchView.SearchViewListener {
        override fun onQueryChanged(searchTerm: String) {
            this@SearchActivity.searchTerm = searchTerm
            binding.recyclerViewDevices.visibility = View.GONE
            binding.recyclerViewRecentAndSuggestion.visibility = View.VISIBLE
            if (searchTerm.isEmpty()) {
                // Show 5 newest recent searches
                showRecentAndSuggestedSearches(
                    sharePreferenceManager.getAllRecentSearches().map { it.query },
                    null
                )
            } else {
                viewModel.searchDevicesByName(searchTerm, manufacturerId)
            }
        }

        override fun onBackButtonClick() {
            finish()
        }

        override fun onClearSearchButtonClick() {
            binding.customViewSearchView.clearSearch()
        }

        override fun onSearchClick() {
            binding.recyclerViewDevices.visibility = View.VISIBLE
            binding.recyclerViewRecentAndSuggestion.visibility = View.GONE
            saveSearch()
            viewModel.searchDevicesByName(searchTerm, manufacturerId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtras()
        setupSearchView()
        setupRecentAndSuggestedSearchRecyclerView()
        setupDevicesRecyclerView()
        initViews()

        observeOnSuggestedSearches()
        observeOnDevices()

        showRecentAndSuggestedSearches(
            sharePreferenceManager.getAllRecentSearches().map { it.query },
            null
        )
    }

    private fun getExtras() {
        manufacturerId = intent.extras?.getString(Constants.EXTRA_MANUFACTURER_ID_KEY, "") ?: ""
    }

    private fun initViews() {
        binding.recyclerViewRecentAndSuggestion.visibility = View.VISIBLE
        binding.recyclerViewDevices.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun setupSearchView() {
        binding.customViewSearchView.apply {
            setListener(searchViewListener)
            focus()
        }
    }

    private fun setupRecentAndSuggestedSearchRecyclerView() {
        recentAndSuggestedSearchAdapter = RecentAndSuggestedSearchRecyclerAdapter(this, recentAndSuggestionListener)
        binding.recyclerViewRecentAndSuggestion.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recentAndSuggestedSearchAdapter
        }
    }

    private fun setupDevicesRecyclerView() {
        deviceAdapter = DeviceRecyclerAdapter(this, deviceListener)
        binding.recyclerViewDevices.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = deviceAdapter
        }
    }

    private fun observeOnSuggestedSearches() {
        viewModel.devicesFoundedByNameLiveData.observe(this, {
            it.data?.let { suggestedSearches ->
                val similarRecentSearches = sharePreferenceManager.findRecentSearchesFromQueryOrderedByNewestToOldest(searchTerm)
                showRecentAndSuggestedSearches(
                    similarRecentSearches.map { recentSearch -> recentSearch.query },
                    suggestedSearches.map { device -> StringHelper.getDeviceFullName(device) }
                )
            }
        })
    }

    private fun showRecentAndSuggestedSearches(recentSearches: List<String>?, suggestedSearches: List<String>?) {
        recentAndSuggestedSearchAdapter.setItems(recentSearches, suggestedSearches)
    }

    private fun observeOnDevices() {
        viewModel.devicesFoundedByNameLiveData.observe(this, {
            if (it.status == LiveDataStatusWrapper.Status.LOADING) {
                showLoading(true)
            } else {
                showLoading(false)
                it.data?.let { devices ->
                    deviceAdapter.setItems(devices)
                }
            }
        })
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun saveSearch() {
        val recentSearch = RecentSearchModel(
            searchTerm,
            Date(),
            true, // TODO
            ""
        )
        sharePreferenceManager.addRecentSearch(recentSearch)
    }
}