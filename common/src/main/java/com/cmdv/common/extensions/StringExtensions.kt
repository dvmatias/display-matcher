package com.cmdv.common.extensions

import android.text.SpannableString
import android.text.Spanned
import java.lang.StringBuilder
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

fun List<String>.parseListWithSymbol(symbol: String? = null): String {
    val builder = StringBuilder()
    for (i in this.indices) {
        if (this.size > 1) {
            symbol?.let { builder.append("$it ") }
        }
         builder.append(this[i])
        if (i != this.lastIndex)  builder.append(System.getProperty("line.separator"))
    }
    return builder.toString()
}