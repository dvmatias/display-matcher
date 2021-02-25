package com.cmdv.common.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        releaseStatusLabels = activity?.resources?.getStringArray(R.array.labels_filter_release_status) ?: arrayOf()
        categoriesLabels = context?.resources?.getStringArray(R.array.labels_filter_categories) ?: arrayOf()
        categoriesIcons = context?.resources?.getIntArray(R.array.icons_filter_categories) ?: intArrayOf()

        setTitle()
        setItems()
        selectedPosition?.let { setSelected(it) }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filterAdapter = FilterRecyclerViewAdapter(activity!!, items, filterType, selectedPosition ?: 0, filterClickListener)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filterAdapter
            addItemDecoration(FilterItemDecorator(activity!!))
        }
    }

    private fun setTitle() {
        binding.textViewTitle.text =
            when (filterType) {
                FilterType.RELEASE_STATUS -> context?.resources?.getString(R.string.text_release_status_filter_bottom_sheet_title)
                FilterType.CATEGORY -> context?.resources?.getString(R.string.text_categories_filter_bottom_sheet_title)
            }
    }

    private fun setItems() {
        when (filterType) {
            FilterType.RELEASE_STATUS -> getReleaseStatusItems()
            FilterType.CATEGORY -> getCategoryItems()
        }
    }

    private fun setSelected(selectedPosition: Int) {
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