package com.cmdv.feature.ui.fragments.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmdv.common.adapters.FilterType
import com.cmdv.common.utils.Constants
import com.cmdv.common.views.CustomFilterSelectorView
import com.cmdv.common.views.FilterBottomSheetFragment
import com.cmdv.core.managers.DeviceFiltersManager
import com.cmdv.core.navigatior.Navigator
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature.R
import com.cmdv.feature.databinding.FragmentDevicesBinding
import com.cmdv.feature.ui.adapters.DeviceRecyclerViewAdapter
import com.cmdv.feature.ui.decorators.DevicesItemDecorator
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import kotlin.math.abs

@ExperimentalCoroutinesApi
class DevicesFragment : Fragment() {

    private lateinit var viewModel: DevicesFragmentViewModel
    private lateinit var binding: FragmentDevicesBinding
    private val navigator: Navigator by inject()

    @Suppress("SpellCheckingInspection")
    private val gson: Gson by inject()
    private lateinit var deviceAdapter: DeviceRecyclerViewAdapter
    private var manufacturer: ManufacturerModel? = null
    private val searchViewMaxBottom by lazy { binding.customViewSearchView.bottom - binding.layoutToolbar.cardViewContainer.bottom + binding.layoutToolbar.cardViewContainer.height }
    private val searchViewHeight by lazy { binding.customViewSearchView.height }

    /**
     *
     */
    private val filterSelectorListener = object : CustomFilterSelectorView.FilterSelectorListener {
        override fun onFilterClick(filterType: FilterType) {
            showBottomSheetFilter(
                filterType,
                when (filterType) {
                    FilterType.RELEASE_STATUS -> DeviceFiltersManager.getDeviceReleaseStatusFilterSelectedPosition(activity!!)
                    FilterType.CATEGORY -> DeviceFiltersManager.getDeviceCategoryFilterSelectedPosition(activity!!)
                }
            )
        }
    }

    /**
     *
     */
    private val bottomSheetFilterListener = object : FilterBottomSheetFragment.BottomSheetFilterListener {
        override fun onReleaseStatusFilterSelected(position: Int) {
            activity?.let { DeviceFiltersManager.setDeviceReleaseStatusFilterSelectedPosition(it, position) }
            setFilterStatus()
        }

        override fun onCategoryFilterSelected(position: Int) {
            activity?.let { DeviceFiltersManager.setDeviceCategoryFilterSelectedPosition(it, position) }
            setFilterStatus()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDevicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DevicesFragmentViewModel::class.java)
        arguments?.getString(Constants.ARG_MANUFACTURER_KEY)?.let {
            manufacturer = gson.fromJson(
                arguments?.getString(Constants.ARG_MANUFACTURER_KEY),
                ManufacturerModel::class.java
            )
        }

        binding.layoutToolbar.imageViewBackButton.setOnClickListener { goToManufacturers() }
        binding.customViewSearchView.setButtonStateListener { goToSearchDevice() }
        setupRecyclerView()
        setupSearchViewMotion()
        handleBackAction()

        manufacturer?.run {
            setManufacturerInfo()
            viewModel.devicesLiveData.observe(viewLifecycleOwner, { devicesStatusWrapper ->
                when (devicesStatusWrapper.status) {
                    LiveDataStatusWrapper.Status.LOADING -> setLoadingStateView()
                    LiveDataStatusWrapper.Status.SUCCESS -> devicesStatusWrapper.data?.run { setInfoStateView(this) }
                    LiveDataStatusWrapper.Status.ERROR -> setErrorStateView()
                }
            })
            viewModel.getDevices(id)
        }
    }

    private fun setupRecyclerView() {
        activity?.let { context ->
            deviceAdapter = DeviceRecyclerViewAdapter(context, ::deviceClickListener, filterSelectorListener)
            binding.recyclerViewDevice.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = deviceAdapter
                addItemDecoration(DevicesItemDecorator())
            }
        }
    }

    private fun setupSearchViewMotion() {
        binding.recyclerViewDevice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val deltaY = (abs(dy) * 0.75).toInt()
                if (dy > 0) {
                    if (binding.customViewSearchView.bottom > binding.layoutToolbar.cardViewContainer.bottom) {
                        binding.customViewSearchView.bottom -= deltaY
                        binding.customViewSearchView.top -= deltaY
                    }
                } else {
                    if (binding.customViewSearchView.bottom < searchViewMaxBottom) {
                        binding.customViewSearchView.bottom += deltaY
                        binding.customViewSearchView.top += deltaY
                    } else {
                        binding.customViewSearchView.bottom = searchViewMaxBottom
                        binding.customViewSearchView.top = (searchViewMaxBottom - searchViewHeight)
                    }
                }
            }
        })
    }

    private fun handleBackAction() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                goToManufacturers()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

    private fun deviceClickListener(deviceId: String) {
        goToDeviceDetails(deviceId)
    }

    private fun setManufacturerInfo() {
        activity?.let {
            Glide.with(this).load(manufacturer?.imageUrl).into(binding.layoutToolbar.imageViewManufacturer)
            binding.layoutToolbar.textViewManufacturer.text = manufacturer?.name
        }
    }

    private fun setLoadingStateView() {
        binding.group.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setInfoStateView(devices: List<DeviceModel>) {
        binding.group.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        deviceAdapter.setItems(devices = devices)
        setFilterStatus()
    }

    private fun setFilterStatus() {
        activity?.let {
            deviceAdapter.setFilterStatus(
                DeviceFiltersManager.getDeviceReleaseStatusFilterSelectedPosition(it),
                DeviceFiltersManager.getDeviceCategoryFilterSelectedPosition(it),
            )
        }
    }

    private fun setErrorStateView() {

    }

    private fun goToDeviceDetails(deviceId: String) {
        activity?.let { activity ->
            val bundle = bundleOf(
                Constants.EXTRA_DEVICE_ID_KEY to deviceId,
                Constants.EXTRA_MANUFACTURER_ID_KEY to manufacturer?.id,
            )
            navigator.toDeviceDetailsActivity(activity, bundle, null, false)
        }
    }

    private fun goToManufacturers() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_devicesFragment_to_manufacturersFragment)
    }

    private fun goToSearchDevice() {
        activity?.let {
            val bundle = bundleOf(
                Constants.EXTRA_MANUFACTURER_ID_KEY to manufacturer?.id,
            )
            navigator.toSearchDevicesActivity(it, bundle, options = null, finish = false)
        }
    }

    private fun showBottomSheetFilter(filterType: FilterType, selectedPosition: Int) {
        activity?.let {
            val bottomSheetDialog: FilterBottomSheetFragment =
                FilterBottomSheetFragment.newInstance(selectedPosition, filterType)
            bottomSheetDialog.setListener(bottomSheetFilterListener)
            bottomSheetDialog.show(it.supportFragmentManager, "Bottom Sheet Dialog Fragment")
        }
    }

}