package com.cmdv.core.managers

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap

private const val SHARED_PREFS_RECENT_SEARCH_NAME = "recent_search_name"

private const val SHARED_PREFS_RECENT_SEARCH_KEY = "recent_search_key"

class SharePreferenceManager(private val context: Context) {
    @Suppress("SpellCheckingInspection")
    private val gson: Gson = Gson()

    /**
     * Store up top 10 search-text/date (key/values) in json format.
     * Existent search-text will generate a date (value) update.
     */
    fun addRecentSearch(searchText: String) {
        val text = searchText.toLowerCase(Locale.ROOT)
        val prefs = context.getSharedPreferences(SHARED_PREFS_RECENT_SEARCH_NAME, Context.MODE_PRIVATE)

        val storedJsonString = prefs.getString(SHARED_PREFS_RECENT_SEARCH_KEY, "")
        val type: Type = object : TypeToken<HashMap<String?, String?>?>() {}.type
        var recentMap: HashMap<String, Date>? = gson.fromJson(storedJsonString, type)
        if (recentMap == null) {
            recentMap = HashMap()
        }

        Log.d("Shit!", "")
        recentMap.run {
            val isAlreadyRecent = containsKey(text)
            if (!isAlreadyRecent) {
                Log.d("Shit!", "No existe.")
                if (size == 10) {
                    // TODO Delete oldest
                }
                this[text] = Date()
            } else {
                this[text] = Date()
            }
        }
        val recentMapString: String = gson.toJson(recentMap, type)
        prefs.edit().putString(SHARED_PREFS_RECENT_SEARCH_KEY, recentMapString).apply()
    }

    fun getRecentSearch() : HashMap<String, Date> {
        val prefs = context.getSharedPreferences(SHARED_PREFS_RECENT_SEARCH_NAME, Context.MODE_PRIVATE)
        val storedJsonString = prefs.getString(SHARED_PREFS_RECENT_SEARCH_KEY, "")
        val type: Type = object : TypeToken<HashMap<String?, String?>?>() {}.type
        var recentMap: HashMap<String, Date>? = gson.fromJson(storedJsonString, type)
        if (recentMap == null) {
            recentMap = HashMap()
        }

        return recentMap
    }


}