package com.cmdv.feature.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmdv.common.utils.Constants
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.feature.R
import com.cmdv.feature.databinding.FragmentInfoBinding
import com.cmdv.feature.databinding.FragmentInfoGeneralBinding
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class GeneralDetailFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private lateinit var bindingGeneral: FragmentInfoGeneralBinding

    private lateinit var device: DeviceModel
    private lateinit var manufacturer: ManufacturerModel
    private val gson: Gson by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            device = gson.fromJson(it.getString(Constants.ARG_DEVICE_KEY), DeviceModel::class.java)
            manufacturer = gson.fromJson(it.getString(Constants.ARG_MANUFACTURER_KEY), ManufacturerModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        bindingGeneral = FragmentInfoGeneralBinding.inflate(inflater, binding.container)

        setGeneralInfo()

        return binding.root
    }

    private fun setGeneralInfo() {
        bindingGeneral.deviceInfoEntryBrand.setInfo(R.string.text_device_detail_info_brand_subtitle, manufacturer.displayName)
    }

    companion object {
        @JvmStatic
        fun newInstance(device: String, manufacturer: String) =
            GeneralDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.ARG_DEVICE_KEY, device)
                    putString(Constants.ARG_MANUFACTURER_KEY, manufacturer)
                }
            }
    }
}