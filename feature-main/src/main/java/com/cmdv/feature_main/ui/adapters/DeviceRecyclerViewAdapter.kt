package com.cmdv.feature_main.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.domain.models.DeviceModel
import com.cmdv.feature_main.databinding.ItemDeviceBinding
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class DeviceRecyclerViewAdapter(
    private val context: Context,
    private val listener: ((String) -> Unit)?
) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.DeviceViewHolder>() {

    private val items: ArrayList<DeviceModel> = arrayListOf()
    private lateinit var manufacturer: String

    fun setItems(devices: List<DeviceModel>, manufacturer: String) {
        items.apply {
            clear()
            addAll(devices.sortedBy { it.name })
        }
        this.manufacturer = manufacturer
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceViewHolder {
        val binding =
            ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DeviceViewHolder,
        position: Int
    ) {
        holder.bindItem(items[position], manufacturer, context, listener)
    }

    override fun getItemCount(): Int = items.size

    /**
     *
     */
    class DeviceViewHolder(private val binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var device: DeviceModel
        private lateinit var manufacturer: String

        fun bindItem(
            device: DeviceModel,
            manufacturer: String,
            context: Context,
            listener: ((String) -> Unit)?
        ) {
            this.device = device
            this.manufacturer = manufacturer
            binding.textViewName.text = getDeviceFullName()
        }

        private fun getDeviceFullName(): String {
            val builder = StringBuilder()
            builder.append(manufacturer.substring(0, 1).capitalize(Locale.ROOT) )
            builder.append(manufacturer.substring(1, manufacturer.length))
            builder.append(" ")
            if (device.name.isNotEmpty()) builder.append("${device.name} ")
            if (device.version.isNotEmpty()) builder.append("${device.version} ")
            if (device.variant.isNotEmpty()) builder.append(device.variant)
            return builder.toString()
        }
    }

}