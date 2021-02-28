package com.cmdv.domain.models

import com.cmdv.common.FilterType

data class FilterModel(
    val filterType: FilterType,
    val label: String,
    val iconRes: Int?,
    val position: Int
)