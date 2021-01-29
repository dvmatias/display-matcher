package com.cmdv.feature_main.ui.fragments.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.data.services.FirebaseDeviceServiceImpl
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature_main.ui.fragments.manufacturers.cancelIfActive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DevicesFragmentViewModel : ViewModel() {
    private var getDevicesJob: Job? = null
    private val mutableDevicesLiveData = MutableLiveData<LiveDataStatusWrapper<List<DeviceModel>>>()
    val devicesLiveData = mutableDevicesLiveData

    fun getDevices(manufacturerId: String) {
        getDevicesJob.cancelIfActive()
        getDevicesJob = viewModelScope.launch {
            FirebaseDeviceServiceImpl.getDevices(manufacturerId).collect { devicesStatusWrapper ->
                mutableDevicesLiveData.value = devicesStatusWrapper
            }
        }
    }

}