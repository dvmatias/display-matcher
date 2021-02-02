package com.cmdv.feature.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.core.extensions.cancelIfActive
import com.cmdv.data.services.FirebaseDeviceServiceImpl
import com.cmdv.data.services.FirebaseManufacturerServiceImpl
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DeviceDetailsViewModel : ViewModel() {
    private var getManufacturerJob: Job? = null
    private val mutableManufacturerLiveData = MutableLiveData<LiveDataStatusWrapper<ManufacturerModel>>()
    val manufacturerLiveData = mutableManufacturerLiveData

    private var getDeviceJob: Job? = null
    private val mutableDeviceLiveData = MutableLiveData<LiveDataStatusWrapper<DeviceModel>>()
    val deviceLiveData = mutableDeviceLiveData

    fun getManufacturer(manufacturerId: String) {
        if (manufacturerId.isEmpty()) {
            mutableManufacturerLiveData.value = LiveDataStatusWrapper(LiveDataStatusWrapper.Status.ERROR, null, "Manufacturer ID can't be null")
            return
        }
        getManufacturerJob.cancelIfActive()
        getManufacturerJob = viewModelScope.launch {
            FirebaseManufacturerServiceImpl.getManufacturer(manufacturerId).collect { manufacturerStatusWrapper ->
                manufacturerLiveData.value = manufacturerStatusWrapper
            }
        }
    }

    fun getDevice(id: String, manufacturerId: String) {
        if (id.isEmpty() || manufacturerId.isEmpty()) {
            mutableDeviceLiveData.value = LiveDataStatusWrapper(LiveDataStatusWrapper.Status.ERROR, null, "Device ID can't be null")
            return
        }
        getDeviceJob.cancelIfActive()
        getDeviceJob = viewModelScope.launch {
            FirebaseDeviceServiceImpl.getDevice(id, manufacturerId).collect { deviceStatusWrapper ->
                mutableDeviceLiveData.value = deviceStatusWrapper
            }
        }
    }

}