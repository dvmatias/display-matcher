package com.cmdv.data.helpers

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private const val THOUSAND = 1000L
    const val PATTERN_MMMM_D_YYYY: String = "MMMM d, yyyy"

    fun getFormattedDateFromSeconds(seconds: Long?, pattern: String): String =
        seconds?.let {
            SimpleDateFormat(pattern, Locale.getDefault()).format(seconds * THOUSAND)
        } ?: kotlin.run { "" }

    fun isInTheFuture(source: String, pattern: String): Boolean {
        val sourceDate: Date? = SimpleDateFormat(pattern, Locale.getDefault()).parse(source)
        return Date().before(sourceDate)
    }

}