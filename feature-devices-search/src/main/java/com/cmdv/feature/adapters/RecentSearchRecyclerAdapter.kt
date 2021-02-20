package com.cmdv.feature.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.feature.databinding.ItemRecentSearchBinding
import java.util.*

class RecentSearchRecyclerAdapter(
    context: Context
) : RecyclerView.Adapter<RecentSearchRecyclerAdapter.RecentSearchViewHolder>() {

    private val items: ArrayList<String> = arrayListOf()

    fun setItems(recentSearch: HashMap<String, Date>) {
        items.clear()
        // TODO order by date
        recentSearch.entries.forEach { entry ->
            items.add(entry.key)
        }
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
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RecentSearchViewHolder(
        private val itemBinding: ItemRecentSearchBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(searchText: String) {
            itemBinding.textViewRecentSearch.text = searchText
        }

    }

}