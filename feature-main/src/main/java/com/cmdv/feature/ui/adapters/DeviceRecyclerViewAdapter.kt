package com.cmdv.feature.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.core.helpers.StringHelper
import com.cmdv.data.helpers.DateHelper
import com.cmdv.data.helpers.DateHelper.PATTERN_YYYY_MMMM
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
            addAll(devices.sortedByDescending { it.launch.release.released })
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
            setDeviceImage(context)
            setReleaseDate(context)
            setResume(context)
            setClickListener(listener)
        }

        private fun setDeviceName() {
            binding.textViewName.text = StringHelper.getDeviceFullName(device)
        }

        private fun setReleaseDate(context: Context) {
            binding.textViewReleaseDate.text = device.launch.release.released?.let { releaseDate ->
                // release date not null, has been released
                when (device.launch.release.status) {
                    ReleaseStatus.AVAILABLE ->
                        context.getString(
                            R.string.placeholder_item_device_release_available,
                            DateHelper.getFormattedDateFromDate(releaseDate, PATTERN_YYYY_MMMM)
                        )
                    ReleaseStatus.CANCELLED -> context.getString(R.string.text_item_device_release_status_cancelled)
                    ReleaseStatus.DISCONTINUED -> context.getString(R.string.text_item_device_release_status_discontinued)
                    else -> ""
                }
            } ?: kotlin.run {
                // device not released yet
                when (device.launch.release.status) {
                    ReleaseStatus.RUMORED -> context.getString(R.string.text_item_device_release_status_rumored)
                    ReleaseStatus.COMING_SOON ->
                        device.launch.release.expected?.let { expectedDate ->
                            if (DateHelper.isInTheFuture(expectedDate)) {
                                context.getString(
                                    R.string.placeholder_item_device_release_status_coming_soon,
                                    DateHelper.getFormattedDateFromDate(expectedDate, PATTERN_YYYY_MMMM)
                                )
                            } else {
                                context.getString(R.string.text_item_device_release_status_delayed)
                            }
                        } ?: kotlin.run{ "" }
                    else -> ""
                }
            }

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

        private fun setClickListener(listener: ((String) -> Unit)?) {
            binding.container.setOnClickListener { listener?.invoke(device.id) }
        }

    }

}