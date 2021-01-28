package com.cmdv.feature_main.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.data.services.FirebaseManufacturerServiceImpl
import com.cmdv.domain.models.ManufacturerModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ManufacturersViewModel : ViewModel() {

    private var getManufacturersJob: Job? = null
    private val mutableManufacturersLiveData = MutableLiveData<List<ManufacturerModel>>()
    val manufacturersLiveData = mutableManufacturersLiveData

    init {
        getManufacturers()
    }

    private fun getManufacturers() {
        getManufacturersJob.cancelIfActive()
        getManufacturersJob = viewModelScope.launch {
            FirebaseManufacturerServiceImpl.getManufacturers().collect { manufacturers ->
                mutableManufacturersLiveData.value = manufacturers
            }
        }
    }
}

private fun Job?.cancelIfActive() {
    this?.let { if (isActive) cancel() }
}