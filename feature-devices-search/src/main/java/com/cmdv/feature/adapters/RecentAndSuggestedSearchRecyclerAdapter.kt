package com.cmdv.feature.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.feature.databinding.ItemRecentSearchBinding
import com.cmdv.feature.databinding.ItemSuggestedSearchBinding
import java.util.*

const val VIEW_TYPE_RECENT_SEARCH = 1
const val VIEW_TYPE_SUGGESTED_SEARCH = 2

class RecentAndSuggestedSearchRecyclerAdapter(
    private val context: Context,
    private val listener: RecentAndSuggestionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val recentSearches: ArrayList<String> = arrayListOf()
    private val suggestedSearches: ArrayList<String> = arrayListOf()

    fun setItems(recentSearches: List<String>?, suggestedSearches: List<String>?) {
        this.recentSearches.apply {
            clear()
            addAll(recentSearches?.take(
                if (!suggestedSearches.isNullOrEmpty())  2 else 5
            ) ?: listOf())

        }
        this.suggestedSearches.apply {
            clear()
            addAll(suggestedSearches?.take(5) ?: listOf())

        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_RECENT_SEARCH ->
                RecentSearchViewHolder(ItemRecentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_SUGGESTED_SEARCH ->
                SuggestedSearchViewHolder(ItemSuggestedSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalStateException("")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecentSearchViewHolder -> holder.bindItem(recentSearches[position], listener)
            is SuggestedSearchViewHolder -> holder.bindItem(suggestedSearches[position - recentSearches.size], listener)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when {
            position < recentSearches.size -> VIEW_TYPE_RECENT_SEARCH
            (position - recentSearches.size) < suggestedSearches.size -> VIEW_TYPE_SUGGESTED_SEARCH
            else -> -1
        }

    override fun getItemCount(): Int = recentSearches.size + suggestedSearches.size

    /**
     * Recent search view holder.
     */
    inner class RecentSearchViewHolder(
        private val itemBinding: ItemRecentSearchBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(recentSearch: String, listener: RecentAndSuggestionListener) {
            itemBinding.textViewRecentSearch.text = recentSearch
            itemBinding.container.setOnClickListener { listener.onRecentSearchClick(recentSearch) }

        }

    }

    /**
     * Suggested search view holder.
     */
    inner class SuggestedSearchViewHolder(
        private val itemBinding: ItemSuggestedSearchBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(suggestionSearch: String, listener: RecentAndSuggestionListener) {
            itemBinding.textViewSuggestionSearch.text = suggestionSearch
            itemBinding.container.setOnClickListener { listener.onSuggestionSearchClick(suggestionSearch) }
        }

    }

    interface RecentAndSuggestionListener {
        fun onRecentSearchClick(recentSearchTerm: String)
        fun onSuggestionSearchClick(suggestionsSearchTerm: String)
    }

}
