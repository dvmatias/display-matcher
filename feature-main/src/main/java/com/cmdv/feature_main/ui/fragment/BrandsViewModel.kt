package com.cmdv.feature_main.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.data.services.FirebaseBrandServiceImpl
import com.cmdv.domain.models.BrandModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class BrandsViewModel : ViewModel() {

    private var getBrandsJob: Job? = null
    private val mutableBrandsLiveData = MutableLiveData<List<BrandModel>>()
    val brandsLiveData = mutableBrandsLiveData

    init {
        getBrands()
    }

    private fun getBrands() {
        getBrandsJob.cancelIfActive()
        getBrandsJob = viewModelScope.launch {
            FirebaseBrandServiceImpl.getBrands().collect { brands ->
                mutableBrandsLiveData.value = brands?.distinctBy { it.name } ?: listOf()
            }
        }
    }
}

private fun Job?.cancelIfActive() {
    this?.let { if (isActive) cancel() }
}