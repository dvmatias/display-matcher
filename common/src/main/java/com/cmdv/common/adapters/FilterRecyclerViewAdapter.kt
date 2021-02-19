package com.cmdv.common.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.common.R
import com.cmdv.common.databinding.ItemCategoryFilterBinding
import com.cmdv.common.databinding.ItemReleaseStatusFilterBinding

enum class FilterType(val type: Int) {
    RELEASE_STATUS(0),
    CATEGORY(1)
}

class FilterRecyclerViewAdapter(
    private val context: Context,
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
        val selector: Drawable? = getSelectorDrawable(position)
        when (this.filterType) {
            FilterType.RELEASE_STATUS -> {
                (holder as ReleaseStatusFilterViewHolder).bindItem(items.entries.elementAt(position), selector)
            }
            FilterType.CATEGORY -> (holder as CategoryFilterViewHolder).bindItem()
        }
    }

    private fun getSelectorDrawable(position: Int): Drawable? =
        when(position) {
            0 -> ContextCompat.getDrawable(context, R.drawable.selector_item_filter_bottom_sheet_top)
            (itemCount - 1) -> ContextCompat.getDrawable(context, R.drawable.selector_item_filter_bottom_sheet_bottom)
            else -> ContextCompat.getDrawable(context, R.drawable.selector_item_filter_bottom_sheet_middle)
        }


    override fun getItemCount(): Int = items.size

    /**
     * Item release status filter view holder class.
     */
    internal class ReleaseStatusFilterViewHolder(private val binding: ItemReleaseStatusFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Map.Entry<String, Int?>, selector: Drawable?) {
            binding.textViewFilterName.text = item.key
            binding.container.background = selector
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