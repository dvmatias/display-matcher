package com.cmdv.data.mappers

import com.cmdv.domain.mappers.BaseMapper
import com.cmdv.domain.models.DeviceModel
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception

private const val DEFAULT_STRING_VALUE = ""
private const val DEFAULT_LONG_VALUE = -1L
private const val DEFAULT_DOUBLE_VALUE = 0.0
private const val FIELD_MANUFACTURER_ID = "manufacturer_id"

object DeviceMapper : BaseMapper<DocumentSnapshot, DeviceModel>() {

    override fun transformEntityToModel(e: DocumentSnapshot): DeviceModel {
        val id: String = e.id
        val manufacturerId: String = e.getStringValue(FIELD_MANUFACTURER_ID)
        val name: String = e.getStringValue("name")
        val variant: String = e.getStringValue("variant")
        val version: String = e.getStringValue("version")
        val displayHeight: Long = e.getLongValue("display_height")
        val displayWidth: Long = e.getLongValue("display_width")
        val displaySize: Double = e.getDoubleValue("display_size")
        val imageUrl: String = e.getStringValue("image_url")

        return DeviceModel(id, manufacturerId, name, variant, version, displayHeight, displayWidth, displaySize, imageUrl)
    }
}

private fun DocumentSnapshot.getStringValue(field: String): String =
    try { this.get(field) as String } catch (e: Exception) { DEFAULT_STRING_VALUE }

private fun DocumentSnapshot.getLongValue(filed: String): Long =
    try { this.get(filed) as Long } catch (e: Exception) { DEFAULT_LONG_VALUE }

private fun DocumentSnapshot.getDoubleValue(filed: String): Double =
    try { this.get(filed) as Double } catch (e: Exception) { DEFAULT_DOUBLE_VALUE }