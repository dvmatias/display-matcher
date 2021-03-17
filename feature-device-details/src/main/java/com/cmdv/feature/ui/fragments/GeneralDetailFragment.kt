package com.cmdv.feature.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmdv.common.utils.Constants
import com.cmdv.core.helpers.StringHelper
import com.cmdv.data.helpers.DateHelper
import com.cmdv.data.helpers.DateHelper.PATTERN_MMMM_YYYY
import com.cmdv.data.helpers.DateHelper.PATTERN_YYYY_MMMM_DD
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.models.ReleaseStatus.*
import com.cmdv.feature.R
import com.cmdv.feature.databinding.FragmentInfoBinding
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class GeneralDetailFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

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
        setGeneralInfo()
        return binding.root
    }

    private fun setGeneralInfo() {
        binding.deviceInfoEntryBrand.setInfo(R.string.text_device_detail_info_brand_subtitle, manufacturer.displayName)
        binding.deviceInfoEntryModel.setInfo(R.string.text_device_detail_info_model_subtitle, StringHelper.getDeviceFullName(device))
        when(device.launch.release.status) {
            AVAILABLE -> device.launch.release.released?.let {
                binding.deviceInfoEntryLaunch.setInfo(R.string.text_device_detail_info_launch_date_subtitle, DateHelper.getFormattedDateFromDate(it, PATTERN_YYYY_MMMM_DD))
            }
            COMING_SOON ->  device.launch.release.expected?.let {
                binding.deviceInfoEntryLaunch.setInfo(R.string.text_device_detail_info_expected_date_subtitle, DateHelper.getFormattedDateFromDate(it, PATTERN_MMMM_YYYY))
            }
            else -> {}
        }
        binding.deviceInfoEntryBodyType.setInfo(R.string.text_device_detail_info_body_type_subtitle, "Need to update FB")
        binding.deviceInfoEntryBatteryCapacity.setInfo(R.string.text_device_detail_info_battery_capacity_subtitle, device.resume.capacity)
        binding.deviceInfoEntryBatteryRemovable.setInfo(R.string.text_device_detail_info_battery_removable_subtitle, "Need to update FB")
        binding.deviceInfoEntryFastCharging.setInfo(R.string.text_device_detail_info_fast_charge_subtitle, "Need to update FB")
        binding.deviceInfoEntryWirelessCharging.setInfo(R.string.text_device_detail_info_wireless_charge_subtitle, "Need to update FB")
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