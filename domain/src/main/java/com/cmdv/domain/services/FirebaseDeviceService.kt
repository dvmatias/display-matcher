package com.cmdv.domain.services

import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import kotlinx.coroutines.flow.Flow

interface FirebaseDeviceService {

    suspend fun getDevicesByManufacturer(manufacturerId: String): Flow<LiveDataStatusWrapper<List<DeviceModel>>>
    suspend fun getDeviceById(id: String): Flow<LiveDataStatusWrapper<DeviceModel>>
    suspend fun getDevicesByName(searchTerm: String, manufacturerId: String): Flow<LiveDataStatusWrapper<List<DeviceModel>>>

}