package com.cmdv.displaymatcher

import android.app.Application
import com.cmdv.core.managers.DeviceFiltersManager
import com.cmdv.core.managers.SharePreferenceManager
import com.cmdv.core.navigatior.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DisplayMatcherApp : Application() {

    private lateinit var navigator: Navigator
    private lateinit var sharePreferenceManager: SharePreferenceManager
    private lateinit var deviceFiltersManager: DeviceFiltersManager

    override fun onCreate() {
        super.onCreate()
        sharePreferenceManager = SharePreferenceManager(this)
        deviceFiltersManager = DeviceFiltersManager(this, sharePreferenceManager)

        initKoin()
        setupDeviceFilters()
    }

    private fun initKoin() {
        startKoin {
//            androidLogger()
            androidContext(this@DisplayMatcherApp)
            modules(appModule, viewModelModule, librariesModule, managersModule)
        }
    }

    private fun setupDeviceFilters() {
        deviceFiltersManager.init()
    }

}