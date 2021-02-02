package com.cmdv.feature.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.core.helpers.StringHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ReleaseStatus
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ItemDeviceBinding

class DeviceRecyclerViewAdapter(
    private val context: Context,
    private val listener: ((String) -> Unit)?
) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.DeviceViewHolder>() {

    private val items: ArrayList<DeviceModel> = arrayListOf()

    fun setItems(devices: List<DeviceModel>) {
        items.apply {
            clear()
            addAll(devices.sortedBy { it.name })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding =
            ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bindItem(items[position], context, listener)
    }

    override fun getItemCount(): Int = items.size

    /**
     *
     */
    class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var device: DeviceModel

        fun bindItem(device: DeviceModel, context: Context, listener: ((String) -> Unit)?) {
            this.device = device

            setDeviceName()
            setReleaseInfo(context)
            setDeviceImage(context)
            setClickListener(listener)
        }

        private fun setDeviceName() {
            binding.textViewName.text = StringHelper.getDeviceFullName(device)
        }

        private fun setReleaseInfo(context: Context) {
            val releaseStatusText = when (device.releaseStatus) {
                ReleaseStatus.RELEASED -> context.getString(R.string.item_device_release_status_released)
                ReleaseStatus.NOT_RELEASED -> context.getString(R.string.item_device_release_status_not_released)
                ReleaseStatus.DELAYED -> context.getString(R.string.item_device_release_status_delayed)
            }
            binding.textViewReleaseDate.text =
                context.getString(
                    R.string.item_device_release_placeholder,
                    releaseStatusText,
                    StringHelper.capitalizeFirstLetterOnly(device.dateRelease),
                )

        }

        private fun setDeviceImage(context: Context) {
            Glide.with(context)
                .load(device.imageUrl)
                .into(binding.imageViewDevice)
        }

        private fun setClickListener(listener: ((String) -> Unit)?) {
            binding.container.setOnClickListener { listener?.invoke(device.id) }
        }

    }

}