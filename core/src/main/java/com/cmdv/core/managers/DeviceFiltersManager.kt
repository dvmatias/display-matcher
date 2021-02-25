package com.cmdv.core.managers

import android.content.Context
import com.cmdv.common.FilterType
import com.cmdv.core.R
import com.cmdv.domain.models.FilterModel

private const val DEFAULT_NO_FILTER_FILTER_SELECTED_POSITION = -1
private const val DEFAULT_RELEASE_STATUS_FILTER_SELECTED_POSITION = 0
private const val DEFAULT_CATEGORY_FILTER_SELECTED_POSITION = 0

class DeviceFiltersManager(
    val context: Context,
    private val sharePreferenceManager: SharePreferenceManager
) {
    var releaseStatusFilters: List<FilterModel>
    var categoryFilters: List<FilterModel>

    init {
        releaseStatusFilters = context.resources.getStringArray(R.array.labels_filter_release_status).map { label ->
            FilterModel(
                FilterType.RELEASE_STATUS,
                label,
                null,
                label.indexOf(label)
            )
        }
        categoryFilters = context.resources.getStringArray(R.array.labels_filter_categories).map { label ->
            FilterModel(
                FilterType.CATEGORY,
                label,
                null,
                label.indexOf(label)
            )
        }
    }

    fun init() {
        sharePreferenceManager.run {
            val releaseStatusFilterSelectedPosition = getDeviceReleaseStatusFilterSelectedPosition()
            if (releaseStatusFilterSelectedPosition == DEFAULT_NO_FILTER_FILTER_SELECTED_POSITION) {
                saveDeviceReleaseStatusFilter(DEFAULT_RELEASE_STATUS_FILTER_SELECTED_POSITION)
            }

            val categoryFilterSelectedPosition = getDeviceCategoryFilterSelectedPosition()
            if (categoryFilterSelectedPosition == DEFAULT_NO_FILTER_FILTER_SELECTED_POSITION) {
                saveDeviceCategoryFilter(DEFAULT_CATEGORY_FILTER_SELECTED_POSITION)
            }
        }
    }

    fun getDeviceReleaseStatusFilterSelectedPosition() =
        sharePreferenceManager.getDeviceReleaseStatusFilterSelectedPosition()

    fun getDeviceCategoryFilterSelectedPosition() =
        sharePreferenceManager.getDeviceCategoryFilterSelectedPosition()

    fun setDeviceReleaseStatusFilterSelectedPosition(position: Int) {
        sharePreferenceManager.saveDeviceReleaseStatusFilter(position)
    }

    fun setDeviceCategoryFilterSelectedPosition(position: Int) {
        sharePreferenceManager.saveDeviceCategoryFilter(position)
    }

}