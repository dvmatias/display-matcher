package com.cmdv.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.common.databinding.ItemCategoryFilterBinding
import com.cmdv.common.databinding.ItemReleaseStatusFilterBinding

enum class FilterType(val type: Int) {
    RELEASE_STATUS(0),
    CATEGORY(1)
}

class FilterRecyclerViewAdapter(
    private val items: Map<String, Int?>,
    private val filterType: FilterType,
    private val selectedPosition: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (this.filterType) {
            FilterType.RELEASE_STATUS -> ReleaseStatusFilterViewHolder(
                ItemReleaseStatusFilterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FilterType.CATEGORY -> CategoryFilterViewHolder(ItemCategoryFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (this.filterType) {
            FilterType.RELEASE_STATUS -> (holder as ReleaseStatusFilterViewHolder).bindItem(items.entries.elementAt(position))
            FilterType.CATEGORY -> (holder as CategoryFilterViewHolder).bindItem()
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * Item release status filter view holder class.
     */
    internal class ReleaseStatusFilterViewHolder(private val binding: ItemReleaseStatusFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Map.Entry<String, Int?>) {
            binding.textViewFilterName.text = item.key
        }
    }

    /**
     * Item category filter view holder class.
     */
    internal class CategoryFilterViewHolder(private val binding: ItemCategoryFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem() {

        }
    }
}