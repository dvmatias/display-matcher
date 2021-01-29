package com.cmdv.displaymatcher

import android.app.Activity
import android.util.Log
import com.cmdv.core.navigatior.Navigator

class NavigatorImpl : Navigator {
    override fun toDeviceDetailsActivity(origin: Activity) {
        // TODO()
        Log.d("NavigatorImpl", "Hi device details activity")
    }
}