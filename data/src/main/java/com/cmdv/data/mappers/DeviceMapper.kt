package com.cmdv.data.mappers

import com.cmdv.data.helpers.DateHelper
import com.cmdv.domain.mappers.BaseMapper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ReleaseStatus
import com.google.firebase.firestore.DocumentSnapshot


private const val DEFAULT_STRING_VALUE = ""
private const val DEFAULT_LONG_VALUE = -1L
private const val DEFAULT_DOUBLE_VALUE = 0.0
private const val DEFAULT_BOOLEAN_VALUE = false
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
        val dateRelease: String = e.getTimeStampValue("date_release")
        val isReleased: Boolean = e.getBooleanValue("released")
        val releaseStatus: ReleaseStatus = getReleasedStatus(isReleased, dateRelease)

        return DeviceModel(
            id,
            manufacturerId,
            name,
            variant,
            version,
            displayHeight,
            displayWidth,
            displaySize,
            imageUrl,
            dateRelease,
            isReleased,
            releaseStatus
        )
    }

    private fun getReleasedStatus(released: Boolean, dateRelease: String): ReleaseStatus =
        when (released) {
            true -> ReleaseStatus.RELEASED
            false -> {
                if (DateHelper.isInTheFuture(dateRelease, DateHelper.PATTERN_MMMM_D_YYYY)) {
                    ReleaseStatus.NOT_RELEASED
                } else {
                    ReleaseStatus.DELAYED
                }
            }
        }
}

private fun DocumentSnapshot.getStringValue(field: String): String =
    try {
        this.get(field) as String
    } catch (e: Exception) {
        DEFAULT_STRING_VALUE
    }

private fun DocumentSnapshot.getLongValue(filed: String): Long =
    try {
        this.get(filed) as Long
    } catch (e: Exception) {
        DEFAULT_LONG_VALUE
    }

private fun DocumentSnapshot.getDoubleValue(filed: String): Double =
    try {
        this.get(filed) as Double
    } catch (e: Exception) {
        DEFAULT_DOUBLE_VALUE
    }

private fun DocumentSnapshot.getTimeStampValue(filed: String): String =
    DateHelper.getFormattedDateFromSeconds(this.getTimestamp(filed)?.seconds, DateHelper.PATTERN_MMMM_D_YYYY)

private fun DocumentSnapshot.getBooleanValue(filed: String): Boolean =
    try {
        this.get(filed) as Boolean
    } catch (e: Exception) {
        DEFAULT_BOOLEAN_VALUE
    }