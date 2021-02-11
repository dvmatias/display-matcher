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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class DeviceDetailsViewModel : ViewModel() {
    private var getManufacturerJob: Job? = null
    private val mutableManufacturerLiveData = MutableLiveData<LiveDataStatusWrapper<ManufacturerModel>>()
    val manufacturerLiveData = mutableManufacturerLiveData

    private var getDeviceJob: Job? = null
    private val mutableDeviceLiveData = MutableLiveData<LiveDataStatusWrapper<DeviceModel>>()
    val deviceLiveData = mutableDeviceLiveData

    private val mutableIsLoadFinishedLiveData = MutableLiveData<Boolean>()
    val isLoadFinishedLiveData = mutableIsLoadFinishedLiveData

    private var isManufacturerGetFinished = false
    private var isDeviceGetFinished = false

    fun getData(id: String, manufacturerId: String) {
        getManufacturer(manufacturerId)
        getDevice(id)
    }

    private fun getManufacturer(manufacturerId: String) {
        if (manufacturerId.isEmpty()) {
            mutableManufacturerLiveData.value = LiveDataStatusWrapper(LiveDataStatusWrapper.Status.ERROR, null, "Manufacturer ID can't be null")
            return
        }
        getManufacturerJob.cancelIfActive()
        getManufacturerJob = viewModelScope.launch {
            FirebaseManufacturerServiceImpl.getManufacturer(manufacturerId).collect {
                mutableManufacturerLiveData.value = it
                isManufacturerGetFinished = it.status == LiveDataStatusWrapper.Status.SUCCESS
                setFinishLoadStatus()
            }
        }
    }

    private fun getDevice(id: String) {
        if (id.isEmpty()) {
            mutableDeviceLiveData.value = LiveDataStatusWrapper(LiveDataStatusWrapper.Status.ERROR, null, "Device ID can't be null")
            return
        }
        getDeviceJob.cancelIfActive()
        getDeviceJob = viewModelScope.launch {
            FirebaseDeviceServiceImpl.getDevice(id).collect {
                mutableDeviceLiveData.value = it
                isDeviceGetFinished = it.status == LiveDataStatusWrapper.Status.SUCCESS
                setFinishLoadStatus()
            }
        }
    }

    private fun setFinishLoadStatus() {
        mutableIsLoadFinishedLiveData.value = isManufacturerGetFinished && isDeviceGetFinished
    }



}