package com.cmdv.feature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.helpers.StringHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.feature.databinding.ItemSuggestionSearchBinding
import java.util.*

private const val MAX_ITEMS_TO_SHOW = 5

class SuggestionSearchRecyclerAdapter(
    private val listener: SuggestionListener
) : RecyclerView.Adapter<SuggestionSearchRecyclerAdapter.SuggestionSearchViewHolder>() {

    private val items: ArrayList<DeviceModel> = arrayListOf()

    fun setItems(suggestionSearches: List<DeviceModel>) {
        items.clear()
        items.addAll(suggestionSearches.let { if (suggestionSearches.size > MAX_ITEMS_TO_SHOW) it.subList(0, MAX_ITEMS_TO_SHOW) else it })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionSearchViewHolder =
        SuggestionSearchViewHolder(
            ItemSuggestionSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SuggestionSearchViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    inner class SuggestionSearchViewHolder(
        private val itemBinding: ItemSuggestionSearchBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(suggestionSearch: DeviceModel, listener: SuggestionListener) {
            itemBinding.textViewSuggestionSearch.text = StringHelper.getDeviceFullName(suggestionSearch)
            itemBinding.container.setOnClickListener { listener.onSuggestionSearchClick(suggestionSearch.resume.name) }
        }

    }

    interface SuggestionListener {
        fun onSuggestionSearchClick(suggestionsSearchTerm: String)
    }

}