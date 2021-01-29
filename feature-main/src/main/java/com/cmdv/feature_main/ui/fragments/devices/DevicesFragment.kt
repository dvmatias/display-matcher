package com.cmdv.feature_main.ui.fragments.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.utils.Constants
import com.cmdv.core.navigatior.Navigator
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature_main.databinding.FragmentDevicesBinding
import com.cmdv.feature_main.ui.adapters.DeviceRecyclerViewAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class DevicesFragment : Fragment() {

    private lateinit var viewModel: DevicesFragmentViewModel
    private lateinit var binding: FragmentDevicesBinding
    private val navigator: Navigator by inject()

    private lateinit var deviceAdapter: DeviceRecyclerViewAdapter

    private lateinit var manufacturerId: String
    private lateinit var manufacturer: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDevicesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DevicesFragmentViewModel::class.java)

        manufacturerId = arguments?.getString(Constants.ARG_MANUFACTURER_ID) ?: ""
        manufacturer = arguments?.getString(Constants.ARG_MANUFACTURER) ?: ""

        if (manufacturerId.isNotEmpty()) {
            viewModel.devicesLiveData.observe(viewLifecycleOwner, { devicesStatusWrapper ->
                when (devicesStatusWrapper.status) {
                    LiveDataStatusWrapper.Status.LOADING -> setLoadingStateView()
                    LiveDataStatusWrapper.Status.SUCCESS -> devicesStatusWrapper.data?.run { setInfoStateView(this) }
                    LiveDataStatusWrapper.Status.ERROR -> setErrorStateView()
                }
            })
            viewModel.getDevices(manufacturerId)
        }
    }

    private fun setupRecyclerView() {
        activity?.let { context ->
            deviceAdapter = DeviceRecyclerViewAdapter(context, ::deviceClickListener)
            binding.recyclerViewDevice.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = deviceAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun deviceClickListener(deviceId: String) {
        goToDeviceDetails(deviceId)
    }

    private fun setLoadingStateView() {
        binding.recyclerViewDevice.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setInfoStateView(devices: List<DeviceModel>) {
        binding.recyclerViewDevice.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        deviceAdapter.setItems(devices, manufacturer)
    }

    private fun setErrorStateView() {

    }

    private fun goToDeviceDetails(deviceId: String) {
        activity?.run { navigator.toDeviceDetailsActivity(this) }
    }

}