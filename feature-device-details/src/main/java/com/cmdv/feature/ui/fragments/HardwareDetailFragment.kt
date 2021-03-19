package com.cmdv.feature.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmdv.common.extensions.cm2ToIn2
import com.cmdv.common.extensions.inToCm
import com.cmdv.common.extensions.ozToG
import com.cmdv.common.extensions.parseListWithSymbol
import com.cmdv.common.utils.Constants
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.feature.R
import com.cmdv.feature.databinding.FragmentHardwareDetailBinding
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class HardwareDetailFragment : Fragment() {
    private lateinit var binding: FragmentHardwareDetailBinding

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
        binding = FragmentHardwareDetailBinding.inflate(inflater, container, false)
        setDisplayInfo()
        setBuildInfo()
        setBatteryInfo()
        setSensorsInfo()
        return binding.root
    }

    private fun setDisplayInfo() {
        with(device.display) {
            with(this.size) {
                // size
                with(this.diagonal) {
                    binding.deviceInfoEntrySize.setInfo(
                        R.string.text_device_detail_info_size_subtitle,
                        String.format(getString(R.string.placeholder_device_detail_info_size), this, this.inToCm())
                    )
                }
                // resolution
                binding.deviceInfoEntryResolution.setInfo(
                    R.string.text_device_detail_info_resolution_subtitle,
                    String.format(getString(R.string.placeholder_device_detail_info_resolution, this.width, this.height))
                )
                // ratio
                binding.deviceInfoEntryRatio.setInfo(R.string.text_device_detail_info_ratio_subtitle, this.ratio)
                // area
                with(this.area) {
                    binding.deviceInfoEntryArea.setInfo(
                        R.string.text_device_detail_info_area_subtitle,
                        String.format(getString(R.string.placeholder_device_detail_info_area), this.cm2ToIn2(), this)
                    )
                }
                // screen to body ratio
                binding.deviceInfoEntryScreenToBody.setInfo(
                    R.string.text_device_detail_info_screen_to_body_subtitle,
                    String.format(getString(R.string.placeholder_device_detail_info_screen_to_body), this.screenToBody)
                )
            }
            // type
            binding.deviceInfoEntryType.setInfo(R.string.text_device_detail_info_type_subtitle, this.type)
            // density
            binding.deviceInfoEntryDensity.setInfo(
                R.string.text_device_detail_info_density_subtitle,
                String.format(getString(R.string.placeholder_device_detail_info_density), this.density)
            )
            // density
            binding.deviceInfoEntryColors.setInfo(R.string.text_device_detail_info_colors_subtitle, "Need to update FB")
        }
    }

    private fun setBuildInfo() {
        with(device.body) {
            // build features
            binding.deviceInfoEntryBuildFeatures.setInfo(
                R.string.text_device_detail_info_build_features_subtitle,
                listOf("Need to update FB", "Need to update FB", "Need to update FB", "Need to update FB", "Need to update FB").parseListWithSymbol()
            )
            // build
            binding.deviceInfoEntryBuild.setInfo(R.string.text_device_detail_info_build_subtitle, this.build)
            with(this.dimensions) {
                // width
                binding.deviceInfoEntryWidth.setInfo(
                    R.string.text_device_detail_info_width_subtitle,
                    String.format(getString(R.string.placeholder_device_detail_info_width), this.width, this.width.inToCm())
                )
                // height
                binding.deviceInfoEntryHeight.setInfo(
                    R.string.text_device_detail_info_height_subtitle,
                    String.format(getString(R.string.placeholder_device_detail_info_height), this.height, this.height.inToCm())
                )
                // thickness
                binding.deviceInfoEntryThickness.setInfo(
                    R.string.text_device_detail_info_thickness_subtitle,
                    String.format(getString(R.string.placeholder_device_detail_info_thickness), this.thickness, this.thickness.inToCm())
                )
            }
            // weight
            binding.deviceInfoEntryWeight.setInfo(
                R.string.text_device_detail_info_weight_subtitle,
                String.format(getString(R.string.placeholder_device_detail_info_weight), this.weight, this.weight.ozToG())
            )
            // sim
            binding.deviceInfoEntrySim.setInfo(
                R.string.text_device_detail_info_sim_subtitle,
                this.sim.parseListWithSymbol("-")
            )
        }
    }

    private fun setBatteryInfo() {
        with(device.battery) {
            binding.deviceInfoEntryCapacity.setInfo(R.string.text_device_detail_info_battery_capacity_subtitle, "Need to update FB")
            binding.deviceInfoEntryTechnology.setInfo(R.string.text_device_detail_info_battery_technology_subtitle, "Need to update FB")
            binding.deviceInfoEntryRemovable.setInfo(R.string.text_device_detail_info_battery_removable_subtitle, this.removable)
            binding.deviceInfoEntryFastCharging.setInfo(
                R.string.text_device_detail_info_battery_fast_charging_subtitle,
                this.fastCharging != -1,
                "Need to update FB"
            )
            binding.deviceInfoWirelessCharging.setInfo(
                R.string.text_device_detail_info_battery_wireless_charging_subtitle,
                this.wirelessCharging != -1,
                "Need to update FB"
            )
            binding.deviceInfoEntryPowerDelivery.setInfo(R.string.text_device_detail_info_battery_power_delivery_subtitle, "Need to update FB")
            binding.deviceInfoEntryStandBy.setInfo(R.string.text_device_detail_info_battery_stand_by_subtitle, "Need to update FB")
            binding.deviceInfoEntryTalkTime.setInfo(R.string.text_device_detail_info_battery_talk_time_subtitle, "Need to update FB")
        }
    }

    private fun setSensorsInfo() {
        with(device.sensors) {
            binding.deviceInfoEntryFingerprint.setInfo(R.string.text_device_detail_info_sensors_fingerprint_subtitle, this.fingerprint)
            binding.deviceInfoEntryProximity.setInfo(R.string.text_device_detail_info_sensors_proximity_subtitle, this.proximity)
            binding.deviceInfoAccelerometer.setInfo(R.string.text_device_detail_info_sensors_accelerometer_subtitle, this.accelerometer)
            binding.deviceInfoEntryMagneticField.setInfo(R.string.text_device_detail_info_sensors_magnetic_field_subtitle, this.magneticField)
            binding.deviceInfoEntryGyroscope.setInfo(R.string.text_device_detail_info_sensors_gyroscope_subtitle, this.gyroscope)
            binding.deviceInfoBarometer.setInfo(R.string.text_device_detail_info_sensors_barometer_subtitle, this.barometer)
            binding.deviceInfoEntryNfc.setInfo(R.string.text_device_detail_info_sensors_nfc_subtitle, this.nfc)
            binding.deviceInfoEntryAmbientLight.setInfo(R.string.text_device_detail_info_sensors_ambient_light_subtitle, this.ambientLight)
            binding.deviceInfoEntryFaceUnlock.setInfo(R.string.text_device_detail_info_sensors_face_unlock_subtitle, this.faceUnlock)
            binding.deviceInfoEntryHallEffect.setInfo(R.string.text_device_detail_info_sensors_hall_effect_subtitle, this.hallEffect)
            binding.deviceInfoEntryPosture.setInfo(R.string.text_device_detail_info_sensors_posture_subtitle, this.posture)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(device: String, manufacturer: String) =
            HardwareDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.ARG_DEVICE_KEY, device)
                    putString(Constants.ARG_MANUFACTURER_KEY, manufacturer)
                }
            }
    }
}