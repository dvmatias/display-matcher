package com.cmdv.feature_main.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.feature_main.databinding.ItemManufacturerBinding
import java.util.*
import kotlin.collections.ArrayList

class ManufacturerRecyclerViewAdapter(val context: Context) : RecyclerView.Adapter<ManufacturerRecyclerViewAdapter.ManufacturerViewHolder>() {

    private val items: ArrayList<ManufacturerModel> = arrayListOf()

    fun setItems(manufacturers: List<ManufacturerModel>) {
        items.apply {
            clear()
            addAll(manufacturers.sortedBy { it.name })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManufacturerViewHolder {
        val binding = ItemManufacturerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManufacturerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManufacturerViewHolder, position: Int) {
        holder.bindItem(items[position], context)
    }

    override fun getItemCount(): Int = items.size

    /**
     * [ManufacturerRecyclerViewAdapter] View holder pattern class.
     */
    class ManufacturerViewHolder(private val binding: ItemManufacturerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(manufacturer: ManufacturerModel, context: Context) {
            binding.textViewName.text = manufacturer.name
            Glide.with(context)
                .load(manufacturer.imageUrl)
                .into(binding.imageViewLogo)
        }

    }

}