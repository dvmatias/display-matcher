package com.cmdv.feature.ui.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.common.FilterType
import com.cmdv.common.R
import com.cmdv.common.databinding.FragmentFilterBottomSheetBinding
import com.cmdv.common.decorators.FilterItemDecorator
import com.cmdv.common.utils.Constants
import com.cmdv.core.managers.DeviceFiltersManager
import com.cmdv.domain.models.FilterModel
import com.cmdv.feature.ui.adapters.FilterRecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterBottomSheetBinding
    private val deviceFiltersManager: DeviceFiltersManager by inject()

    private lateinit var filters: List<FilterModel>
    private lateinit var filterAdapter: FilterRecyclerViewAdapter

    private var selectedPosition: Int? = null
    private lateinit var filterType: FilterType
    private lateinit var listener: BottomSheetFilterListener

    private val filterClickListener = object : FilterRecyclerViewAdapter.FilterClickListener {
        override fun onReleaseStatusFilterSelected(position: Int) {
            listener.onReleaseStatusFilterSelected(position)
            this@FilterBottomSheetFragment.dismiss()
        }

        override fun onCategoryFilterSelected(position: Int) {
            listener.onCategoryFilterSelected(position)
            this@FilterBottomSheetFragment.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)

        getExtras()
        setFilters()
        setTitle()
        return binding.root
    }

    private fun getExtras() {
        filterType =
            when (arguments?.getInt(Constants.ARG_FILTER_TYPE)) {
                FilterType.RELEASE_STATUS.type -> FilterType.RELEASE_STATUS
                FilterType.CATEGORY.type -> FilterType.CATEGORY
                else -> throw IllegalAccessException("")
            }
        selectedPosition = arguments?.getInt(Constants.ARG_FILTER_SELECTED_POSITION)
    }

    private fun setFilters() {
        filters = when (filterType) {
            FilterType.RELEASE_STATUS -> deviceFiltersManager.releaseStatusFilters
            FilterType.CATEGORY -> deviceFiltersManager.categoryFilters
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filterAdapter = FilterRecyclerViewAdapter(requireContext(), filters, filterType, selectedPosition ?: 0, filterClickListener)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filterAdapter
            addItemDecoration(FilterItemDecorator(requireContext()))
        }
    }

    private fun setTitle() {
        binding.textViewTitle.text =
            when (filterType) {
                FilterType.RELEASE_STATUS -> context?.resources?.getString(R.string.text_release_status_filter_bottom_sheet_title)
                FilterType.CATEGORY -> context?.resources?.getString(R.string.text_categories_filter_bottom_sheet_title)
            }
    }

    fun setListener(listener: BottomSheetFilterListener) {
        this.listener = listener
    }

    companion object {

        fun newInstance(
            selectedPosition: Int,
            filterType: FilterType
        ): FilterBottomSheetFragment =
            FilterBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(Constants.ARG_FILTER_SELECTED_POSITION, selectedPosition)
                    putInt(Constants.ARG_FILTER_TYPE, filterType.type)
                }
            }

    }

    interface BottomSheetFilterListener {
        fun onReleaseStatusFilterSelected(position: Int)
        fun onCategoryFilterSelected(position: Int)
    }
}