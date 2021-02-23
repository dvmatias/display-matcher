package com.cmdv.feature.ui.fragments.manufacturers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.common.extensions.cancelIfActive
import com.cmdv.data.services.FirebaseManufacturerServiceImpl
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ManufacturersFragmentViewModel : ViewModel() {
    private var getManufacturersJob: Job? = null
    private val mutableManufacturersLiveData = MutableLiveData<LiveDataStatusWrapper<List<ManufacturerModel>>>()
    val manufacturersLiveData = mutableManufacturersLiveData

    init {
        getManufacturers()
    }

    private fun getManufacturers() {
        getManufacturersJob.cancelIfActive()
        getManufacturersJob = viewModelScope.launch {
            FirebaseManufacturerServiceImpl.getManufacturers().collect { manufacturersStatusWrapper ->
                mutableManufacturersLiveData.value = manufacturersStatusWrapper
            }
        }
    }
}