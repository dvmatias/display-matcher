package com.cmdv.domain.services

import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import kotlinx.coroutines.flow.Flow

interface FirebaseDeviceService {

    suspend fun getDevices(manufacturerId: String): Flow<LiveDataStatusWrapper<List<DeviceModel>>>
    suspend fun getDevice(id: String, manufacturerId: String): Flow<LiveDataStatusWrapper<DeviceModel>>

}