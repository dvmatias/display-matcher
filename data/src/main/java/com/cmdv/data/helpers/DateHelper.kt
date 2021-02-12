package com.cmdv.data.helpers

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private const val THOUSAND = 1000L
    const val PATTERN_MMMM_D_YYYY: String = "MMMM d, yyyy"

    fun getFormattedDateFromSeconds(seconds: Long?, pattern: String): String =
        seconds?.let {
            SimpleDateFormat(pattern, Locale.getDefault()).format(seconds * THOUSAND)
        } ?: kotlin.run { "" }

    fun getDateFromTimestamp(timestamp: Timestamp): Date? {
        val dateString = getFormattedDateFromSeconds(timestamp.seconds, PATTERN_MMMM_D_YYYY)
        return SimpleDateFormat(PATTERN_MMMM_D_YYYY, Locale.getDefault()).parse(dateString)
    }

    fun isInTheFuture(source: String, pattern: String): Boolean {
        val sourceDate: Date? = SimpleDateFormat(pattern, Locale.getDefault()).parse(source)
        return Date().before(sourceDate)
    }

}