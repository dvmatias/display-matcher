package com.cmdv.domain.services

import com.cmdv.domain.models.ManufacturerModel
import kotlinx.coroutines.flow.Flow

interface FirebaseManufacturerService {

    suspend fun getManufacturers(): Flow<List<ManufacturerModel>?>

}