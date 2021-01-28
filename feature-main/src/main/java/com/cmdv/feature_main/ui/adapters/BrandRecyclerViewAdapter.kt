package com.cmdv.feature_main.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.domain.models.BrandModel
import com.cmdv.feature_main.databinding.ItemBrandBinding

class BrandRecyclerViewAdapter(val context: Context) : RecyclerView.Adapter<BrandRecyclerViewAdapter.BrandViewHolder>() {

    private val items: ArrayList<BrandModel> = arrayListOf()

    fun setItems(brands: List<BrandModel>) {
        items.apply {
            clear()
            addAll(brands)
        }.run { notifyDataSetChanged() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding = ItemBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bindItem(items[position], context)
    }

    override fun getItemCount(): Int = items.size

    /**
     * [BrandRecyclerViewAdapter] View holder pattern class.
     */
    class BrandViewHolder(private val binding: ItemBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(brand: BrandModel, context: Context) {
            binding.textViewName.text = brand.name
            Glide.with(context)
                .load(brand.imageUrl)
                .into(binding.imageViewLogo)
        }

    }

}