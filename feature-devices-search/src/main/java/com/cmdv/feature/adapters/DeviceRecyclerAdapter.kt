package com.cmdv.feature.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.core.helpers.StringHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ItemDevicBinding
import java.util.*

class DeviceRecyclerAdapter(
    private val context: Context,
    private val listener: DeviceListener
) : RecyclerView.Adapter<DeviceRecyclerAdapter.DeviceViewHolder>() {

    private val items: ArrayList<DeviceModel> = arrayListOf()

    fun setItems(devices: List<DeviceModel>) {
        items.clear()
        items.addAll(devices)
        items.sortedBy { it.model.name }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder =
        DeviceViewHolder(
            ItemDevicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bindItem(items[position], context, listener)
    }

    override fun getItemCount(): Int = items.size

    /**
     * Device item view holder
     */
    class DeviceViewHolder(private val binding: ItemDevicBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var device: DeviceModel

        fun bindItem(device: DeviceModel, context: Context, listener: DeviceListener) {
            this.device = device

            setDeviceName()
            setDeviceImage(context)
            setReleaseDate(context)
            setResume(context)
            setClickListener(listener)
        }

        private fun setDeviceName() {
            binding.textViewName.text = StringHelper.getDeviceFullName(device)
        }

        private fun setReleaseDate(context: Context) {
            binding.textViewReleaseDate.text = StringHelper.getReleaseDateString(context, device.launch.release)
        }

        private fun setResume(context: Context) {
            with(device.resume) {
//                binding.textViewDisplay.text = context.getString(R.string.placeholder_item_device_display_size, display)
                binding.textViewCamera.text =
                    context.getString(
                        R.string.placeholder_item_device_camera,
                        camera.replace("+", "-").replace("MP", "").replace(" ", "").trim { it <= ' ' }
                    )
                binding.textViewRam.text = ram.replace("/", "-").trim { it <= ' ' }
                binding.textViewCapacity.text = capacity
            }
        }

        private fun setDeviceImage(context: Context) {
            Glide.with(context)
                .load(device.thumbnail)
                .into(binding.imageViewThumbnail)
        }

        private fun setClickListener(listener: DeviceListener) {
            binding.container.setOnClickListener { listener.onDeviceClick(device.id) }
        }

    }

    interface DeviceListener {
        fun onDeviceClick(deviceId: String)
    }
}