package com.cmdv.core.navigatior

import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat

interface Navigator {
    fun toDeviceDetailsActivity(
        origin: Activity,
        bundle: Bundle?,
        options: ActivityOptionsCompat?,
        finish: Boolean
    )
}