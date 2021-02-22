package com.cmdv.core.managers

import android.content.Context
import com.cmdv.domain.models.RecentSearchModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList

private const val SHARED_PREFS_RECENT_SEARCH_NAME = "recent_search_name"

private const val SHARED_PREFS_RECENT_SEARCH_KEY = "recent_search_key"

class SharePreferenceManager(private val context: Context) {
    @Suppress("SpellCheckingInspection")
    private val gson: Gson = Gson()

    /**
     * Store up top 10 search-text/date (key/values) in json format.
     * Existent search-text will generate a date (value) update.
     */
    fun addRecentSearch(recentSearch: RecentSearchModel) {
        val prefs = context.getSharedPreferences(SHARED_PREFS_RECENT_SEARCH_NAME, Context.MODE_PRIVATE)

        val storedJsonString = prefs.getString(SHARED_PREFS_RECENT_SEARCH_KEY, "")
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
        prefs.edit().putString(SHARED_PREFS_RECENT_SEARCH_KEY, recentSearchesString).apply()
    }

    fun getAllRecentSearches(): ArrayList<RecentSearchModel> {
        val prefs = context.getSharedPreferences(SHARED_PREFS_RECENT_SEARCH_NAME, Context.MODE_PRIVATE)
        val storedJsonString = prefs.getString(SHARED_PREFS_RECENT_SEARCH_KEY, "")

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
            it.takeIf { it.query.contains(query) }?.let { suggestionsSearches.add(it) }
        }
        return suggestionsSearches
    }

}