package com.cmdv.core.helpers

import com.cmdv.domain.models.DeviceModel
import java.util.*

object StringHelper {

    fun getDeviceFullName(device: DeviceModel, manufacturer: String): String {
        val builder = StringBuilder()
        builder.append("${capitalizeFirstLetterOnly(manufacturer)} ")
        if (device.name.isNotEmpty()) builder.append("${device.name} ")
        if (device.version.isNotEmpty()) builder.append("${device.version} ")
        if (device.variant.isNotEmpty()) builder.append(device.variant)
        return builder.toString()
    }

    fun capitalizeFirstLetterOnly(text: String): String =
        if (text.isNotEmpty())
            text.substring(0, 1).capitalize(Locale.getDefault()) + text.substring(1).toLowerCase(Locale.getDefault())
        else
            ""

}