package com.cmdv.domain.models

import java.util.*

data class RecentSearchModel(
    val query: String,
    val date: Date,
    val success: Boolean,
    val manufacturerId: String
)
