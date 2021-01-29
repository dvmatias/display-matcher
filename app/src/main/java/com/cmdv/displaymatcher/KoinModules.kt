package com.cmdv.displaymatcher

import com.cmdv.core.navigatior.Navigator
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { NavigatorImpl() }
}