package com.cmdv.feature.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmdv.common.utils.Constants
import com.cmdv.domain.models.DeviceModel
import com.cmdv.feature.databinding.FragmentInfoBinding
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

    private var device: DeviceModel? = null
    private val gson: Gson by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            device = gson.fromJson(it.getString(Constants.ARG_DEVICE_KEY), DeviceModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)

        // TODO do al stuffs here

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(device: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.ARG_DEVICE_KEY, device)
                }
            }
    }
}