package com.cmdv.feature_main.ui.fragments.models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.utils.Constants
import com.cmdv.feature_main.databinding.FragmentModelsBinding
import com.cmdv.feature_main.ui.adapters.DeviceRecyclerViewAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DevicesFragment : Fragment() {

    private lateinit var viewModel: DevicesFragmentViewModel
    private lateinit var binding: FragmentModelsBinding

    private lateinit var deviceAdapter: DeviceRecyclerViewAdapter
    private lateinit var manufacturer: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModelsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DevicesFragmentViewModel::class.java)

        val manufacturerId: String = arguments?.getString(Constants.ARG_MANUFACTURER_ID) ?: ""
        val manufacturer: String = arguments?.getString(Constants.ARG_MANUFACTURER) ?: ""

        if (manufacturerId.isNotEmpty()) {
            viewModel.devicesLiveData.observe(viewLifecycleOwner, { devices ->
                deviceAdapter.setItems(devices, manufacturer)
            })
            viewModel.getDevices(manufacturerId)
        }
    }

    private fun setupRecyclerView() {
        activity?.let { context ->
            deviceAdapter = DeviceRecyclerViewAdapter(context, null)
            binding.recyclerViewDevice.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = deviceAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

}