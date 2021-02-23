package com.cmdv.common.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.R
import com.cmdv.common.adapters.FilterRecyclerViewAdapter
import com.cmdv.common.adapters.FilterType
import com.cmdv.common.databinding.FragmentFilterBottomSheetBinding
import com.cmdv.common.decorators.FilterItemDecorator
import com.cmdv.common.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterBottomSheetBinding
    private lateinit var releaseStatusLabels: Array<out String>
    private lateinit var categoriesLabels: Array<out String>
    private lateinit var categoriesIcons: IntArray
    private var items: MutableMap<String, Int?> = mutableMapOf()
    private var filterType: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)

        releaseStatusLabels = activity?.resources?.getStringArray(R.array.labels_filter_release_status) ?: arrayOf()
        categoriesLabels = context?.resources?.getStringArray(R.array.labels_filter_categories) ?: arrayOf()
        categoriesIcons = context?.resources?.getIntArray(R.array.icons_filter_categories) ?: intArrayOf()

        filterType = arguments?.getInt(Constants.ARG_FILTER_TYPE)
        filterType?.let {
            setTitle()
            setItems()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FilterRecyclerViewAdapter(activity!!, items, FilterType.RELEASE_STATUS, 0)
            addItemDecoration(FilterItemDecorator(activity!!))
        }
    }

    private fun setTitle() {
        binding.textViewTitle.text =
            when (filterType) {
                FilterType.RELEASE_STATUS.type -> context?.resources?.getString(R.string.text_release_status_filter_bottom_sheet_title)
                FilterType.CATEGORY.type -> context?.resources?.getString(R.string.text_categories_filter_bottom_sheet_title)
                else -> throw IllegalStateException("Wrong filter type")
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

        fun newInstance(selectedPosition: Int, filterType: FilterType): FilterBottomSheetFragment =
            FilterBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constants.ARG_FILTER_SELECTED_POSITION, selectedPosition)
                    putInt(Constants.ARG_FILTER_TYPE, filterType.type)
                }
            }

    }
}