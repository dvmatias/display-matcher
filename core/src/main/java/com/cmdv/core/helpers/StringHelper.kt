package com.cmdv.core.helpers

import android.content.Context
import com.cmdv.core.R
import com.cmdv.common.extensions.capitalizeFirstLetters
import com.cmdv.data.helpers.DateHelper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ReleaseStatus

object StringHelper {

    fun getDeviceFullName(device: DeviceModel): String {
        val builder = StringBuilder()
        if (device.model.name.isNotEmpty()) builder.append("${device.model.name} ")
        if (device.model.version.isNotEmpty()) builder.append("${device.model.version} ")
        if (device.model.variant.isNotEmpty()) builder.append(device.model.variant)
        return builder.toString().trim { it <= ' ' }
    }

    fun getReleaseDateString(context: Context, release: DeviceModel.LaunchModel.ReleaseModel) =
        release.released?.let { releaseDate ->
            val result = when (release.status) {
                ReleaseStatus.AVAILABLE ->
                    context.getString(
                        R.string.placeholder_item_device_release_available,
                        DateHelper.getFormattedDateFromDate(releaseDate, DateHelper.PATTERN_MMMM_D_YYYY)
                    )
                ReleaseStatus.CANCELLED -> context.getString(R.string.text_item_device_release_status_cancelled)
                ReleaseStatus.DISCONTINUED -> context.getString(R.string.text_item_device_release_status_discontinued)
                else -> ""
            }
            result.capitalizeFirstLetters()
        } ?: kotlin.run {
            val result = when (release.status) {
                ReleaseStatus.RUMORED -> context.getString(R.string.text_item_device_release_status_rumored)
                ReleaseStatus.COMING_SOON ->
                    release.expected?.let { expectedDate ->
                        if (DateHelper.isInTheFuture(sourceDate = expectedDate)) {
                            context.getString(
                                R.string.placeholder_item_device_release_status_coming_soon,
                                DateHelper.getFormattedDateFromDate(expectedDate, DateHelper.PATTERN_MMMM_YYYY)
                            )
                        } else {
                            context.getString(R.string.text_item_device_release_status_delayed)
                        }
                    } ?: kotlin.run { "" }
                else -> ""
            }
            result.capitalizeFirstLetters()
        }

}