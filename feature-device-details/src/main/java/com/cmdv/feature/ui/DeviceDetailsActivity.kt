package com.cmdv.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ActivityDeviceDetailsMainBinding

class DeviceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceDetailsMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceDetailsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addConstraintSetAnimation()
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
}