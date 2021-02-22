package com.cmdv.feature.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.domain.models.RecentSearchModel
import com.cmdv.feature.databinding.ItemRecentSearchBinding
import java.util.*

class RecentSearchRecyclerAdapter(
    context: Context,
    private val listener: RecentSearchListener
) : RecyclerView.Adapter<RecentSearchRecyclerAdapter.RecentSearchViewHolder>() {

    private val items: ArrayList<RecentSearchModel> = arrayListOf()

    fun setItems(recentSearches: ArrayList<RecentSearchModel>) {
        items.clear()
        items.addAll(recentSearches.subList(0, 5))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder =
        RecentSearchViewHolder(
            ItemRecentSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    inner class RecentSearchViewHolder(
        private val itemBinding: ItemRecentSearchBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(recentSearch: RecentSearchModel, listener: RecentSearchListener) {
            itemBinding.textViewRecentSearch.text = recentSearch.query
            itemBinding.container.setOnClickListener { listener.onRecentSearchClick(recentSearch.query) }

        }

    }

    interface RecentSearchListener {
        fun onRecentSearchClick(query: String)
    }

}