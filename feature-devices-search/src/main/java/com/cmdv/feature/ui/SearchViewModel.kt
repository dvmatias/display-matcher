package com.cmdv.feature.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.common.extensions.cancelIfActive
import com.cmdv.data.services.FirebaseDeviceServiceImpl
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Suppress("EXPERIMENTAL_API_USAGE")
class SearchViewModel : ViewModel() {
    private var getDevicesByNameJob: Job? = null
    private val mutableDevicesFoundedByNameLiveData = MutableLiveData<LiveDataStatusWrapper<List<DeviceModel>>>()
    val devicesFoundedByNameLiveData = mutableDevicesFoundedByNameLiveData

    fun searchDevicesByName(searchTerm: String, manufacturerId: String) {
        getDevicesByNameJob.run {
            cancelIfActive()
            viewModelScope.safeLaunch {
                FirebaseDeviceServiceImpl.getDevicesByName(searchTerm, manufacturerId).collect {
                    mutableDevicesFoundedByNameLiveData.value = it
                }
            }
        }
    }

}

fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit): Job {
    return this.launch {
        try {
            block()
        } catch (cancellationException: CancellationException) {
            Log.e("SearchViewModel","Coroutine error", cancellationException)
        } catch (exception: Exception) {
            Log.e("SearchViewModel","Coroutine error", exception)
        }
    }
}