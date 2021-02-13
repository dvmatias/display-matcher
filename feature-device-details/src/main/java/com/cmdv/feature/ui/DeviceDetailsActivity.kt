package com.cmdv.feature.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.CompositePageTransformer
import com.bumptech.glide.Glide
import com.cmdv.common.utils.Constants
import com.cmdv.core.helpers.DimensHelper
import com.cmdv.core.helpers.StringHelper
import com.cmdv.core.helpers.TextAnimationHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ActivityDeviceDetailsMainBinding
import com.cmdv.feature.ui.adapters.ImagesViewPagerAdapter
import com.cmdv.feature.ui.fragments.InfoFragment
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

private const val ANIM_DURATION = 350L

@Suppress("EXPERIMENTAL_API_USAGE")
class DeviceDetailsActivity : AppCompatActivity() {
    private val viewModel: DeviceDetailsViewModel by viewModel()
    private lateinit var binding: ActivityDeviceDetailsMainBinding

    private val gson: Gson by inject()

    private lateinit var deviceId: String
    private lateinit var manufacturerId: String
    private lateinit var device: DeviceModel
    private lateinit var pagerAdapter: ImagesViewPagerAdapter
    private var isLoadFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceDetailsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtras()
        initViews()
        setupPager()
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

    private fun setupPager() {
        pagerAdapter = ImagesViewPagerAdapter(this, binding.viewPagerDeviceImages)
        binding.viewPagerDeviceImages.adapter = pagerAdapter

        val transformer = CompositePageTransformer()
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = (0.55f + r * 0.45F)
            page.scaleX = (0.55f + r * 0.45F)
        }

        binding.viewPagerDeviceImages.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(transformer)
        }
    }

    private fun addConstraintSetAnimation() {
        val mainConstraint = ConstraintSet()
        mainConstraint.clone(binding.root)

        val infoConstraint = ConstraintSet()
        infoConstraint.clone(this, R.layout.activity_device_details_info)

        val compareConstraint = ConstraintSet()
        compareConstraint.clone(this, R.layout.activity_device_details_compare)

        binding.viewPagerDeviceImages.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            mainConstraint.applyTo(binding.root)
            if (isLoadFinished) hideLoading()

            TextAnimationHelper.animateTextSize(
                this,
                binding.textViewDeviceName.textSize,
                DimensHelper.spToPx(this, 20F),
                ANIM_DURATION,
                binding.textViewDeviceName
            )

            TextAnimationHelper.animateTextSize(
                this,
                binding.textViewDisplaySize.textSize,
                DimensHelper.spToPx(this, 18F),
                ANIM_DURATION,
                binding.textViewDisplaySize, binding.textViewCameraPhoto, binding.textViewRam, binding.textViewCapacity
            )
        }

        binding.imageButtonInfo.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            infoConstraint.applyTo(binding.root)

            TextAnimationHelper.animateTextSize(
                this,
                binding.textViewDeviceName.textSize,
                DimensHelper.spToPx(this, 16F),
                ANIM_DURATION,
                binding.textViewDeviceName
            )

            TextAnimationHelper.animateTextSize(
                this,
                binding.textViewDisplaySize.textSize,
                DimensHelper.spToPx(this, 14F),
                ANIM_DURATION,
                binding.textViewDisplaySize, binding.textViewCameraPhoto, binding.textViewRam, binding.textViewCapacity
            )

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
                device = it.data!!
                setResume()
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

    private fun setResume() {
        with(device) {
            binding.textViewDeviceName.text = StringHelper.getDeviceFullName(this)
            setImages()
            setReleaseStatus()
            setDisplay(resume)
            setCamera(resume)
            setRam(resume)
            setBattery(resume)
        }

    }

    private fun setImages() {
        pagerAdapter.setItems(device.images)
    }

    private fun setReleaseStatus() {
        binding.textViewRelease.text = StringHelper.getReleaseDateString(this, device.launch.release)
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

    private fun setBattery(resume: DeviceModel.ResumeModel) {
        binding.textViewCapacity.text = resume.capacity
        binding.textViewTechnology.text = resume.technology
    }

    private fun inflateInfoFragment() {
        val fragment = InfoFragment.newInstance(gson.toJson(device))
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(binding.fameInfoFragmentContainer.id, fragment)
        transaction.commit()
    }

}