package com.cmdv.feature.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.feature.databinding.ItemManufacturerBinding

class ManufacturerRecyclerViewAdapter(
    private val context: Context,
    private val listener: (String, String) -> Unit
) : RecyclerView.Adapter<ManufacturerRecyclerViewAdapter.ManufacturerViewHolder>() {

    private val items: ArrayList<ManufacturerModel> = arrayListOf()

    fun setItems(manufacturers: List<ManufacturerModel>) {
        items.apply {
            clear()
            addAll(manufacturers.sortedBy { it.name })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManufacturerViewHolder {
        val binding =
            ItemManufacturerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManufacturerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManufacturerViewHolder, position: Int) {
        holder.bindItem(items[position], context, listener)
    }

    override fun getItemCount(): Int = items.size

    /**
     * [ManufacturerRecyclerViewAdapter] View holder pattern class.
     */
    class ManufacturerViewHolder(private val binding: ItemManufacturerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(
            manufacturer: ManufacturerModel,
            context: Context,
            listener: (String, String) -> Unit
        ) {
            with(manufacturer) {
                Glide.with(context)
                    .load(imageUrl)
                    .into(binding.imageViewLogo)
                binding.textViewName.text = name
                binding.cardViewContainer.setOnClickListener { listener.invoke(id, name) }
            }
        }

    }

}