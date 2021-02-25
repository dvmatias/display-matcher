package com.cmdv.core.managers

import android.content.Context
import java.lang.ref.WeakReference

private const val DEFAULT_NO_FILTER_FILTER_SELECTED_POSITION = -1
private const val DEFAULT_RELEASE_STATUS_FILTER_SELECTED_POSITION = 0
private const val DEFAULT_CATEGORY_FILTER_SELECTED_POSITION = 0

object DeviceFiltersManager {

    fun init(context: Context) {
        val sharePreferenceManager = WeakReference(SharePreferenceManager(context))
        sharePreferenceManager.get()?.run {
            if (getDeviceReleaseStatusFilterSelectedPosition() == DEFAULT_NO_FILTER_FILTER_SELECTED_POSITION) {
                saveDeviceReleaseStatusFilter(DEFAULT_RELEASE_STATUS_FILTER_SELECTED_POSITION)
            }
            if (getDeviceCategoryFilterSelectedPosition() == DEFAULT_NO_FILTER_FILTER_SELECTED_POSITION) {
                saveDeviceCategoryFilter(DEFAULT_CATEGORY_FILTER_SELECTED_POSITION)
            }
        }
    }

    fun getDeviceReleaseStatusFilterSelectedPosition(context: Context) =
        WeakReference(SharePreferenceManager(context)).get()?.getDeviceReleaseStatusFilterSelectedPosition() ?: DEFAULT_RELEASE_STATUS_FILTER_SELECTED_POSITION

    fun getDeviceCategoryFilterSelectedPosition(context: Context) =
        WeakReference(SharePreferenceManager(context)).get()?.getDeviceCategoryFilterSelectedPosition() ?: DEFAULT_CATEGORY_FILTER_SELECTED_POSITION

    fun setDeviceReleaseStatusFilterSelectedPosition(context: Context, position: Int) {
        WeakReference(SharePreferenceManager(context)).get()?.saveDeviceReleaseStatusFilter(position)
    }

    fun setDeviceCategoryFilterSelectedPosition(context: Context, position: Int) {
        WeakReference(SharePreferenceManager(context)).get()?.saveDeviceCategoryFilter(position)
    }

}