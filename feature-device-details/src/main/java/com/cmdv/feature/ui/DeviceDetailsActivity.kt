package com.cmdv.feature.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.cmdv.common.utils.Constants
import com.cmdv.data.mappers.ManufacturerMapper
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ActivityDeviceDetailsMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

@Suppress("EXPERIMENTAL_API_USAGE")
class DeviceDetailsActivity : AppCompatActivity() {
    private val viewModel: DeviceDetailsViewModel by viewModel()
    private lateinit var binding: ActivityDeviceDetailsMainBinding

    private lateinit var deviceId: String
    private lateinit var manufacturerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceDetailsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtras()
        addConstraintSetAnimation()
        observeOnDevice()
    }

    private fun getExtras() {
        deviceId = intent.extras?.getString(Constants.EXTRA_DEVICE_ID_KEY, "") ?: ""
        manufacturerId = intent.extras?.getString(Constants.EXTRA_MANUFACTURER_ID_KEY, "") ?: ""
    }

    private fun addConstraintSetAnimation() {
        val mainConstraint = ConstraintSet()
        mainConstraint.clone(binding.root)

        val infoConstraint = ConstraintSet()
        infoConstraint.clone(this, R.layout.activity_device_details_info)

        val compareConstraint = ConstraintSet()
        compareConstraint.clone(this, R.layout.activity_device_details_compare)

        binding.imageViewDevice.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            mainConstraint.applyTo(binding.root)
        }

        binding.imageButtonInfo.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            infoConstraint.applyTo(binding.root)
        }

        binding.imageViewCompareButton.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            compareConstraint.applyTo(binding.root)
        }
    }

    private fun observeOnDevice() {
        viewModel.manufacturerLiveData.observe(this, { manufacturerStatusWrapper ->
            if (manufacturerStatusWrapper.status == LiveDataStatusWrapper.Status.SUCCESS) {
                setManufacturerInfo(manufacturerStatusWrapper.data)
            }
        })
        viewModel.deviceLiveData.observe(this, { deviceStatusWrapper ->
            Log.d("Shit", "Doh!")
        })
        viewModel.getManufacturer(manufacturerId)
        viewModel.getDevice(deviceId, manufacturerId)
    }

    private fun setManufacturerInfo(manufacturer: ManufacturerModel?) {
        manufacturer?.run {
            Glide.with(this@DeviceDetailsActivity)
                .load(imageUrl)
                .into(binding.imageViewManufacturer)
            binding.textViewManufacturer.text = name
            

            // TODO refactor this
            binding.frameLoading.visibility = View.GONE
        }
    }
}