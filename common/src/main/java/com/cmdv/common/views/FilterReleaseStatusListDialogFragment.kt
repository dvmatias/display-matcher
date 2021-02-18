package com.cmdv.common.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.R
import com.cmdv.common.adapters.FilterRecyclerViewAdapter
import com.cmdv.common.adapters.FilterType
import com.cmdv.common.databinding.FragmentFilterReleaseStatusListDialogListDialogBinding
import com.cmdv.common.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterReleaseStatusListDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterReleaseStatusListDialogListDialogBinding
    private lateinit var releaseStatusLabels: Array<out String>
    private lateinit var categoriesLabels: Array<out String>
    private lateinit var categoriesIcons: IntArray
    private var items: MutableMap<String, Int?> = mutableMapOf()
    private var filterType: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterReleaseStatusListDialogListDialogBinding.inflate(inflater, container, false)

        releaseStatusLabels = activity?.resources?.getStringArray(R.array.labels_filter_release_status) ?: arrayOf()
        categoriesLabels = context?.resources?.getStringArray(R.array.labels_filter_categories) ?: arrayOf()
        categoriesIcons = context?.resources?.getIntArray(R.array.icons_filter_categories) ?: intArrayOf()

        filterType = arguments?.getInt(Constants.ARG_FILTER_TYPE)
        filterType?.let { setItems() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FilterRecyclerViewAdapter(items, FilterType.RELEASE_STATUS, 0)
        }
    }

    private fun setItems() {
        when (filterType) {
            FilterType.RELEASE_STATUS.type -> getReleaseStatusItems()
            FilterType.CATEGORY.type -> getCategoryItems()
            else -> throw IllegalStateException("Wrong filter type")
        }
    }

    private fun getReleaseStatusItems() {
        releaseStatusLabels.forEach { key ->
            items[key] = null
        }
    }

    private fun getCategoryItems() {
        categoriesLabels.forEach { key ->
            items[key] = null
        }
    }

    companion object {

        fun newInstance(selectedPosition: Int, filterType: FilterType): FilterReleaseStatusListDialogFragment =
            FilterReleaseStatusListDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constants.ARG_FILTER_SELECTED_POSITION, selectedPosition)
                    putInt(Constants.ARG_FILTER_TYPE, filterType.type)
                }
            }

    }
}