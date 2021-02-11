package com.cmdv.feature.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentTransaction
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.cmdv.common.utils.Constants
import com.cmdv.core.helpers.StringHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ActivityDeviceDetailsMainBinding
import com.cmdv.feature.ui.fragments.InfoFragment
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


@Suppress("EXPERIMENTAL_API_USAGE")
class DeviceDetailsActivity : AppCompatActivity() {
    private val viewModel: DeviceDetailsViewModel by viewModel()
    private lateinit var binding: ActivityDeviceDetailsMainBinding

    private val gson: Gson by inject()

    private lateinit var deviceId: String
    private lateinit var manufacturerId: String
    private var isLoadFinished = false
    private var device: DeviceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceDetailsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtras()
        initViews()
        addConstraintSetAnimation()
        observeData()
    }

    private fun getExtras() {
        deviceId = intent.extras?.getString(Constants.EXTRA_DEVICE_ID_KEY, "") ?: ""
        manufacturerId = intent.extras?.getString(Constants.EXTRA_MANUFACTURER_ID_KEY, "") ?: ""
    }

    private fun initViews() {
        hideLoading()
        binding.imageViewBackButton.setOnClickListener { finish() }
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
            if (isLoadFinished) hideLoading()
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

    private fun hideLoading() {
        binding.frameLoading.visibility = View.GONE
    }

    private fun showLoading() {
        binding.frameLoading.visibility = View.VISIBLE
    }

    private fun observeData() {
        showLoading()
        viewModel.manufacturerLiveData.observe(this, {
            if (it.status == LiveDataStatusWrapper.Status.SUCCESS) {
                setManufacturerInfo(it.data)
            }
        })
        viewModel.deviceLiveData.observe(this, {
            if (it.status == LiveDataStatusWrapper.Status.SUCCESS) {
                device = it.data
                setDeviceMinimalisticDetails()
                inflateInfoFragment()
            }
        })
        viewModel.isLoadFinishedLiveData.observe(this, { isLoadFinished ->
            if (isLoadFinished) {
                this.isLoadFinished = isLoadFinished
                hideLoading()
            }
        })
        viewModel.getData(deviceId, manufacturerId)
    }

    private fun setManufacturerInfo(manufacturer: ManufacturerModel?) {
        manufacturer?.let {
            Glide.with(this@DeviceDetailsActivity).load(it.imageUrl).into(binding.imageViewManufacturer)
            binding.textViewManufacturer.text = it.name
        }
    }

    private fun setDeviceMinimalisticDetails() {
        device?.let {
            binding.textViewDeviceName.text = StringHelper.getDeviceFullName(it)
            setImage()
            setReleaseStatus()
            setDisplay(it.resume)
            setCamera(it.resume)
            setRam(it.resume)
        }
    }

    private fun setImage() {
        device?.let {
            Glide.with(this@DeviceDetailsActivity).load(it.thumbnail).into(binding.imageViewDevice)
        }
    }

    private fun setReleaseStatus() {
        device?.let {
//            val releaseStatusText = when (it.releaseStatus) {
//                ReleaseStatus.RELEASED -> getString(R.string.text_item_device_release_status_released)
//                ReleaseStatus.NOT_RELEASED -> getString(R.string.text_item_device_release_status_not_released)
//                ReleaseStatus.DELAYED -> getString(R.string.text_item_device_release_status_delayed)
//            }
//            binding.textViewRelease.text =
//                getString(
//                    R.string.placeholder_item_device_release,
//                    releaseStatusText,
//                    StringHelper.capitalizeFirstLetterOnly(it.dateRelease),
//                )
        }
    }

    private fun setDisplay(resume: DeviceModel.ResumeModel) {
        binding.textViewDisplaySize.text = String.format(
            getString(R.string.placeholder_device_detail_display_size),
            resume.display
        )
        binding.textViewDisplayResolution.text = String.format(
            getString(R.string.placeholder_device_detail_display_resolution),
            resume.resolution
        )

    }

    private fun setCamera(resume: DeviceModel.ResumeModel) {
        binding.textViewCameraPhoto.text = resume.camera
        binding.textViewCameraVideo.text = resume.video
    }

    private fun setRam(resume: DeviceModel.ResumeModel) {
        binding.textViewRam.text = resume.ram
        binding.textViewChipSet.text = resume.chipset

    }

    private fun inflateInfoFragment() {
        val fragment = InfoFragment.newInstance(gson.toJson(device))
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(binding.fameInfoFragmentContainer.id, fragment)
        transaction.commit()
    }

}