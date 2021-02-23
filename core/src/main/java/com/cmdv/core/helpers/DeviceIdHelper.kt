package com.cmdv.core.helpers

import android.util.Base64
import android.util.Base64.decode
import com.google.gson.Gson
import com.google.gson.JsonObject

object DeviceIdHelper {

    fun getDeviceManufacturer(deviceIdBase64: String): String {
        val jsonString = decodeDeviceId(deviceIdBase64)
        val json = Gson().fromJson(jsonString, JsonObject::class.java)
        return json.get("manufacturer").asString
    }

    fun getDeviceName(deviceIdBase64: String): String {
        val jsonString = decodeDeviceId(deviceIdBase64)
        val json = Gson().fromJson(jsonString, JsonObject::class.java)
        return json.get("name").asString
    }

    private fun decodeDeviceId(deviceIdBase64: String): String =
        decode(deviceIdBase64, Base64.DEFAULT).toString(charset("UTF-8"))

}
