@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.cmdv.displaymatcher

import com.cmdv.core.navigatior.Navigator
import com.cmdv.feature.ui.DeviceDetailsViewModel
import com.google.gson.Gson
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { NavigatorImpl() }
}

val viewModelModule = module {
    viewModel { DeviceDetailsViewModel() }
}

val librariesModule = module {
    single { Gson() }
}