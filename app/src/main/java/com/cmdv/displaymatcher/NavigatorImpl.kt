package com.cmdv.displaymatcher

import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.cmdv.core.extensions.navigateTo
import com.cmdv.core.navigatior.Navigator
import com.cmdv.feature.ui.DeviceDetailsActivity
import com.cmdv.feature.ui.SearchActivity

class NavigatorImpl : Navigator {
    override fun toDeviceDetailsActivity(origin: Activity, bundle: Bundle?, options: ActivityOptionsCompat?, finish: Boolean) {
        origin.navigateTo<DeviceDetailsActivity>(bundle, options, finish)
    }

    override fun toSearchDevicesActivity(origin: Activity, bundle: Bundle?, options: ActivityOptionsCompat?, finish: Boolean) {
        origin.navigateTo<SearchActivity>(bundle, options, finish)
    }
}