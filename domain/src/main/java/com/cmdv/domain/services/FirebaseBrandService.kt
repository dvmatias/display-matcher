package com.cmdv.domain.services

import com.cmdv.domain.models.BrandModel
import kotlinx.coroutines.flow.Flow

interface FirebaseBrandService {

    suspend fun getBrands(): Flow<List<BrandModel>?>

}