package com.cmdv.core.managers

import android.content.Context
import com.cmdv.domain.models.RecentSearchModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList

private const val RECENT_SEARCHES_NAME = "sp_recent_searches_name"
private const val DEVICE_FILTER_NAME = "sp_device_filter_name"

private const val RECENT_SEARCHES_KEY = "sp_recent_search_key"
private const val DEVICE_RELEASE_STATUS_FILTER_POSITION_KEY = "sp_device_release_status_filter_position_key"
private const val DEVICE_CATEGORY_FILTER_POSITION_KEY = "sp_device_category_filter_position_key"

class SharePreferenceManager(private val context: Context) {
    @Suppress("SpellCheckingInspection")
    private val gson: Gson = Gson()

    fun getDeviceReleaseStatusFilterSelectedPosition(): Int {
        val prefs = context.getSharedPreferences(DEVICE_FILTER_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(DEVICE_RELEASE_STATUS_FILTER_POSITION_KEY, -1)
    }

    fun getDeviceCategoryFilterSelectedPosition(): Int {
        val prefs = context.getSharedPreferences(DEVICE_FILTER_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(DEVICE_CATEGORY_FILTER_POSITION_KEY, -1)
    }

    fun saveDeviceReleaseStatusFilter(position: Int) {
        val prefs = context.getSharedPreferences(DEVICE_FILTER_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(DEVICE_RELEASE_STATUS_FILTER_POSITION_KEY, position).apply()
    }

    fun saveDeviceCategoryFilter(position: Int) {
        val prefs = context.getSharedPreferences(DEVICE_FILTER_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(DEVICE_CATEGORY_FILTER_POSITION_KEY, position).apply()
    }

    /**
     * Store up top 10 search-text/date (key/values) in json format.
     * Existent search-text will generate a date (value) update.
     */
    fun addRecentSearch(recentSearch: RecentSearchModel) {
        val prefs = context.getSharedPreferences(RECENT_SEARCHES_NAME, Context.MODE_PRIVATE)

        val storedJsonString = prefs.getString(RECENT_SEARCHES_KEY, "")
        val type: Type = object : TypeToken<ArrayList<RecentSearchModel>>() {}.type
        var recentSearches: ArrayList<RecentSearchModel>? = gson.fromJson(storedJsonString, type)
        if (recentSearches == null) {
            recentSearches = arrayListOf()
        }

        val currentQuery = recentSearch.query
        val sortedRecentSearches: MutableList<RecentSearchModel> = mutableListOf()
        if (recentSearches.isNotEmpty()) {
            sortedRecentSearches.addAll(recentSearches.sortedWith(compareBy { it.date }))
        }

        var exists = false
        sortedRecentSearches.forEach { if (it.query == currentQuery) exists = true }
        if (exists) {
            for (i in 0 until sortedRecentSearches.size) {
                if (sortedRecentSearches[i].query == currentQuery) {
                    sortedRecentSearches.removeAt(i)
                    sortedRecentSearches.add(i, recentSearch)
                }
            }
        } else {
            if (sortedRecentSearches.size == 200) {
                sortedRecentSearches[199] = recentSearch
            } else {
                sortedRecentSearches.add(recentSearch)
            }
        }

        val recentSearchesString: String = gson.toJson(sortedRecentSearches.sortedWith(compareByDescending { it.date }) as MutableList, type)
        prefs.edit().putString(RECENT_SEARCHES_KEY, recentSearchesString).apply()
    }

    fun getAllRecentSearches(): ArrayList<RecentSearchModel> {
        val prefs = context.getSharedPreferences(RECENT_SEARCHES_NAME, Context.MODE_PRIVATE)
        val storedJsonString = prefs.getString(RECENT_SEARCHES_KEY, "")

        val type: Type = object : TypeToken<ArrayList<RecentSearchModel>?>() {}.type
        var recentSearches: ArrayList<RecentSearchModel>? = gson.fromJson(storedJsonString, type)
        if (recentSearches == null) {
            recentSearches = arrayListOf()
        }

        return recentSearches
    }

    fun findRecentSearchesFromQueryOrderedByNewestToOldest(query: String): List<RecentSearchModel> =
        findAllRecentSearchesFromQuery(query).sortedWith(compareByDescending { it.date })

    private fun findAllRecentSearchesFromQuery(query: String): ArrayList<RecentSearchModel> {
        val suggestionsSearches = arrayListOf<RecentSearchModel>()
        getAllRecentSearches().forEach {
            it.takeIf { it.query.contains(query,true) }?.let { suggestionsSearches.add(it) }
        }
        return suggestionsSearches
    }

}