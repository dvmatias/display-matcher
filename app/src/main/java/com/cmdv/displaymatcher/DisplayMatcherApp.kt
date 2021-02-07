package com.cmdv.displaymatcher

import android.app.Application
import com.cmdv.core.navigatior.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DisplayMatcherApp : Application() {

    private lateinit var navigator: Navigator

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
//            androidLogger()
            androidContext(this@DisplayMatcherApp)
            modules(appModule, viewModelModule, librariesModule)
        }
    }
}