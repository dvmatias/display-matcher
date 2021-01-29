package com.cmdv.domain.services

import com.cmdv.domain.models.DeviceModel
import kotlinx.coroutines.flow.Flow

interface FirebaseDeviceService {

    suspend fun getDevices(manufacturerId: String): Flow<List<DeviceModel>?>

}