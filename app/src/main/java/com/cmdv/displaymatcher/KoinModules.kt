package com.cmdv.displaymatcher

import com.cmdv.core.navigatior.Navigator
import com.cmdv.feature.ui.DeviceDetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { NavigatorImpl() }
}

val viewModelModule = module {
    viewModel { DeviceDetailsViewModel() }
}