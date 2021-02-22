package com.cmdv.feature.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.common.databinding.ItemDeviceBinding
import com.cmdv.common.views.CustomFilterSelectorView
import com.cmdv.core.helpers.StringHelper
import com.cmdv.data.helpers.DateHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ReleaseStatus
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ItemFilterHeaderBinding
import com.cmdv.feature.databinding.ItemLoadingFooterBinding

class DeviceRecyclerViewAdapter(
    private val context: Context,
    private val listener: ((String) -> Unit)?,
    private val filterClickListener: CustomFilterSelectorView.OnFilterClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType(val viewType: Int) {
        HEADER_FILTER(0),
        DEVICE(1),
        FOOTER_LOADING(2)
    }

    private val items: ArrayList<DeviceModel> = arrayListOf()
    private var isMoreItems = false

    fun setItems(devices: List<DeviceModel>, isMoreDevices: Boolean = false) {
        this.isMoreItems = isMoreDevices
        items.apply {
            clear()
            addAll(getOrderedDeviceList(devices))
        }
        notifyDataSetChanged()
    }

    fun addItems(devices: List<DeviceModel>, isMoreDevices: Boolean) {
        this.isMoreItems = isMoreDevices
        items.addAll(devices)
        notifyItemRangeChanged(items.size - devices.size, itemCount)
    }

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> ViewType.HEADER_FILTER.viewType
            (itemCount - 1) -> ViewType.FOOTER_LOADING.viewType
            else -> ViewType.DEVICE.viewType
        }

    private fun getOrderedDeviceList(devices: List<DeviceModel>): List<DeviceModel> {
        val released = devices.filter { it.launch.release.status == ReleaseStatus.AVAILABLE }.sortedByDescending { it.launch.release.released }
        val announced = devices.filter { it.launch.release.status == ReleaseStatus.COMING_SOON && it.launch.release.expected != null }
            .sortedByDescending { it.launch.release.expected }
        val rumored = devices.filter { it.launch.release.status == ReleaseStatus.RUMORED && it.launch.announced == null }
            .sortedByDescending { it.launch.release.expected }
        return rumored.plus(announced).plus(released)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ViewType.HEADER_FILTER.viewType ->
                HeaderFilterViewHolder(ItemFilterHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ViewType.DEVICE.viewType ->
                DeviceViewHolder(ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ViewType.FOOTER_LOADING.viewType ->
                FooterLoadingViewHolder(ItemLoadingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> DeviceViewHolder(ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.HEADER_FILTER.viewType -> (holder as HeaderFilterViewHolder).bindItem(filterClickListener)
            ViewType.DEVICE.viewType -> (holder as DeviceViewHolder).bindItem(items[position - 1], context, listener)
            ViewType.FOOTER_LOADING.viewType -> (holder as FooterLoadingViewHolder).bindItem()
        }
    }

    override fun getItemCount(): Int = items.size + 2

    /**
     * Header item view holder
     */
    class HeaderFilterViewHolder(private val binding: ItemFilterHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(filterClickListener: CustomFilterSelectorView.OnFilterClickListener) {
            binding.filterSelector.setupListeners(filterClickListener)
        }

    }

    /**
     * Device item view holder
     */
    class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var device: DeviceModel

        fun bindItem(device: DeviceModel, context: Context, listener: ((String) -> Unit)?) {
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

        private fun setClickListener(listener: ((String) -> Unit)?) {
            binding.container.setOnClickListener { listener?.invoke(device.id) }
        }

    }

    /**
     * Footer item view holder
     */
    class FooterLoadingViewHolder(private val binding: ItemLoadingFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem() {

        }
    }

}