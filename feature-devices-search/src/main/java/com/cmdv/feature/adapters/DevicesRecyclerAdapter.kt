package com.cmdv.feature.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.common.databinding.ItemDeviceBinding
import com.cmdv.core.helpers.StringHelper
import com.cmdv.data.helpers.DateHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ReleaseStatus
import com.cmdv.feature.R
import java.util.*

class DevicesRecyclerAdapter(
    private val context: Context,
    private val listener: DeviceListener
) : RecyclerView.Adapter<DevicesRecyclerAdapter.DeviceViewHolder>() {

    private val items: ArrayList<DeviceModel> = arrayListOf()

    fun setItems(devices: List<DeviceModel>) {
        items.clear()
        items.addAll(devices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder =
        DeviceViewHolder(
            ItemDeviceBinding.inflate(
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
    class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var device: DeviceModel

        fun bindItem(device: DeviceModel, context: Context, listener: DeviceListener) {
            this.device = device

            setDeviceName()
            setInfoIcon(context)
            setDeviceImage(context)
            setReleaseDate(context)
            setResume(context)
            setClickListener(listener)
        }

        private fun setDeviceName() {
            binding.textViewName.text = StringHelper.getDeviceFullName(device)
        }

        private fun setInfoIcon(context: Context) {
            var iconRes: Int? = null
            if (device.launch.release.status == ReleaseStatus.COMING_SOON) {
                device.launch.release.expected?.let { expectedDate ->
                    iconRes = if (DateHelper.isInTheFuture(sourceDate = expectedDate)) {
                        R.drawable.ic_expected_18dp
                    } else {
                        R.drawable.ic_delayed_18dp
                    }
                }
            }
            if (device.launch.release.status == ReleaseStatus.RUMORED) {
                iconRes = R.drawable.ic_rumored_18dp
            }

            iconRes?.let { icon ->
                binding.imageViewInfoIcon.apply {
                    visibility = View.VISIBLE
                    setImageDrawable(ContextCompat.getDrawable(context, icon))
                }
            } ?: kotlin.run { binding.imageViewInfoIcon.visibility = View.GONE }
        }

        private fun setReleaseDate(context: Context) {
            binding.textViewReleaseDate.text = StringHelper.getReleaseDateString(context, device.launch.release)
        }

        private fun setDeviceImage(context: Context) {
            Glide.with(context)
                .load(device.thumbnail)
                .into(binding.imageViewThumbnail)
        }

        private fun setResume(context: Context) {
            with(device.resume) {
                binding.textViewDisplay.text = context.getString(R.string.placeholder_item_device_display_size, display)
                binding.textViewCamera.text =
                    context.getString(
                        R.string.placeholder_item_device_camera,
                        camera.replace("+", "-").replace("MP", "").replace(" ", "").trim { it <= ' ' }
                    )
                binding.textViewRam.text = ram.replace("/", "-").trim { it <= ' ' }
                binding.textViewCapacity.text = capacity
            }
        }

        private fun setClickListener(listener: DeviceListener) {
            binding.container.setOnClickListener { listener.onDeviceClick(device.id) }
        }

    }

    interface DeviceListener {
        fun onDeviceClick(deviceId: String)
    }
}