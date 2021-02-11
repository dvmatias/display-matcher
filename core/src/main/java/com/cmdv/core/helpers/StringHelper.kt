package com.cmdv.core.helpers

import com.cmdv.domain.models.DeviceModel
import java.util.*

object StringHelper {

    fun getDeviceFullName(device: DeviceModel): String {
        val builder = StringBuilder()
        if (device.model.name.isNotEmpty()) builder.append("${device.model.name} ")
        if (device.model.version.isNotEmpty()) builder.append("${device.model.version} ")
        if (device.model.variant.isNotEmpty()) builder.append(device.model.variant)
        return builder.toString()
    }

    fun getDeviceFullNameWithManufacturer(device: DeviceModel): String {
        val builder = StringBuilder()
        builder.append("${capitalizeFirstLetterOnly(DeviceIdHelper.getDeviceManufacturer(device.id))} ")
        if (device.model.name.isNotEmpty()) builder.append("${device.model.name} ")
        if (device.model.version.isNotEmpty()) builder.append("${device.model.version} ")
        if (device.model.variant.isNotEmpty()) builder.append(device.model.variant)
        return builder.toString()
    }

    fun capitalizeFirstLetterOnly(text: String): String =
        if (text.isNotEmpty())
            text.substring(0, 1).capitalize(Locale.getDefault()) + text.substring(1).toLowerCase(Locale.getDefault())
        else
            ""

}