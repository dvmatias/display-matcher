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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cmdv.common.utils.Constants
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

@ExperimentalCoroutinesApi
class DevicesFragment : Fragment() {

    private lateinit var viewModel: DevicesFragmentViewModel
    private lateinit var binding: FragmentDevicesBinding
    private val navigator: Navigator by inject()
    private val gson: Gson by inject()

    private lateinit var deviceAdapter: DeviceRecyclerViewAdapter

    private var manufacturer: ManufacturerModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding.layoutToolbar.imageViewSearchButton.setOnClickListener { goToSearchDevice() }
        binding.viewSearchShape.setOnClickListener { goToSearchDevice() }
        setupRecyclerView()
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
            deviceAdapter = DeviceRecyclerViewAdapter(context, ::deviceClickListener)
            binding.recyclerViewDevice.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = deviceAdapter
                addItemDecoration(DevicesItemDecorator())
            }
        }
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
        binding.layoutToolbar.cardViewContainer.visibility = View.GONE
        binding.recyclerViewDevice.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setInfoStateView(devices: List<DeviceModel>) {
        binding.layoutToolbar.cardViewContainer.visibility = View.VISIBLE
        binding.recyclerViewDevice.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        deviceAdapter.setItems(devices)
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
            navigator.toSearchDevicesActivity(it, bundle = null, options = null, finish = false)
        }
    }

}