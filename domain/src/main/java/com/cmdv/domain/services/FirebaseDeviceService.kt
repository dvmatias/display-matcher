package com.cmdv.domain.services

import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import kotlinx.coroutines.flow.Flow

interface FirebaseDeviceService {

    suspend fun getDevices(manufacturerId: String): Flow<LiveDataStatusWrapper<List<DeviceModel>>>

}