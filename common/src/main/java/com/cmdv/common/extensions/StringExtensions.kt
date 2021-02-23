package com.cmdv.common.extensions

import java.util.*

fun String.capitalizeFirstLetterOnly(): String =
    if (this.isNotEmpty())
        this.substring(0, 1).capitalize(Locale.getDefault()) + this.substring(1).toLowerCase(Locale.getDefault())
    else
        ""

fun String.capitalizeFirstLetters(): String =
    if (isNotEmpty()) {
        val split = arrayListOf<String>()
        split(' ').forEach { split.add(it.capitalizeFirstLetterOnly()) }
        split.joinToString(
            separator = " ",
            prefix = "",
            postfix = "",
            limit = -1,
            truncated = "",
            transform = null
        )
    } else {
        ""
    }