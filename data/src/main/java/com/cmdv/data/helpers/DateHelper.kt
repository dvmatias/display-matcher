package com.cmdv.data.helpers

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private const val THOUSAND = 1000L
    const val PATTERN_MMMM_D_YYYY: String = "MMMM d, yyyy"
    const val PATTERN_YYYY_MMMM: String = "yyyy, MMMM"
    const val PATTERN_YYYY_MMMM_DD: String = "yyyy, MMMM dd"

    fun getFormattedDateFromSeconds(seconds: Long?, pattern: String): String =
        seconds?.let {
            SimpleDateFormat(pattern, Locale.getDefault()).format(seconds * THOUSAND)
        } ?: kotlin.run { "" }

    fun getFormattedDateFromDate(source: Date, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(source)

    fun getDateFromTimestamp(timestamp: Timestamp): Date? {
        if (timestamp.seconds == 0L && timestamp.nanoseconds == 0) return null
        val dateString = getFormattedDateFromSeconds(timestamp.seconds, PATTERN_MMMM_D_YYYY)
        return SimpleDateFormat(PATTERN_MMMM_D_YYYY, Locale.getDefault()).parse(dateString)
    }

    fun isInTheFuture(source: Date): Boolean =
        Date().before(source)


}