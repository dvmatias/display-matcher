package com.cmdv.domain.services

import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import kotlinx.coroutines.flow.Flow

interface FirebaseManufacturerService {

    suspend fun getManufacturer(id: String): Flow<LiveDataStatusWrapper<ManufacturerModel>>
    suspend fun getManufacturers(): Flow<LiveDataStatusWrapper<List<ManufacturerModel>>>

}