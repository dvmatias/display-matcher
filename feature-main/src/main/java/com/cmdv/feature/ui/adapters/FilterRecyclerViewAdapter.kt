package com.cmdv.feature.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.common.FilterType
import com.cmdv.common.R
import com.cmdv.common.databinding.ItemCategoryFilterBinding
import com.cmdv.common.databinding.ItemReleaseStatusFilterBinding
import com.cmdv.domain.models.FilterModel


class FilterRecyclerViewAdapter(
    private val context: Context,
    private val items: List<FilterModel>,
    private val filterType: FilterType,
    private val selectedPosition: Int,
    private val listener: FilterClickListener
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
        val isSelected = position == selectedPosition
        when (this.filterType) {
            FilterType.RELEASE_STATUS -> {
                (holder as ReleaseStatusFilterViewHolder).bindItem(
                    position,
                    items[position],
                    selector,
                    listener,
                    isSelected
                )
            }
            FilterType.CATEGORY ->
                (holder as CategoryFilterViewHolder).bindItem(
                    position,
                    items[position],
                    selector,
                    listener,
                    isSelected
                )
        }
    }

    private fun getSelectorDrawable(position: Int): Drawable? =
        when (position) {
            0 -> ContextCompat.getDrawable(context, R.drawable.selector_item_filter_bottom_sheet_top)
            (itemCount - 1) -> ContextCompat.getDrawable(context, R.drawable.selector_item_filter_bottom_sheet_bottom)
            else -> ContextCompat.getDrawable(context, R.drawable.selector_item_filter_bottom_sheet_middle)
        }


    override fun getItemCount(): Int = items.size

    /**
     * Item release status filter view holder class.
     */
    internal class ReleaseStatusFilterViewHolder(private val binding: ItemReleaseStatusFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(
            position: Int,
            item: FilterModel,
            selector: Drawable?,
            listener: FilterClickListener,
            isSelected: Boolean
        ) {
            binding.textViewFilterName.text = item.label
            binding.container.background = selector
            binding.container.isSelected = isSelected
            binding.container.setOnClickListener { listener.onReleaseStatusFilterSelected(position) }
        }
    }

    /**
     * Item category filter view holder class.
     */
    internal class CategoryFilterViewHolder(private val binding: ItemCategoryFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(
            position: Int,
            item: FilterModel,
            selector: Drawable?,
            listener: FilterClickListener,
            isSelected: Boolean
        ) {
            binding.textViewFilterName.text = item.label
            binding.container.background = selector
            binding.container.isSelected = isSelected
            binding.container.setOnClickListener { listener.onCategoryFilterSelected(position) }
        }
    }

    interface FilterClickListener {
        fun onReleaseStatusFilterSelected(position: Int)
        fun onCategoryFilterSelected(position: Int)
    }
}