package com.cmdv.displaymatcher

import android.app.Application
import com.cmdv.core.managers.DeviceFiltersManager
import com.cmdv.core.navigatior.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DisplayMatcherApp : Application() {

    private lateinit var navigator: Navigator

    override fun onCreate() {
        super.onCreate()
        initKoin()
        setupDeviceFilters()
    }

    private fun initKoin() {
        startKoin {
//            androidLogger()
            androidContext(this@DisplayMatcherApp)
            modules(appModule, viewModelModule, librariesModule)
        }
    }

    private fun setupDeviceFilters() {
        DeviceFiltersManager.init(this)
    }

}