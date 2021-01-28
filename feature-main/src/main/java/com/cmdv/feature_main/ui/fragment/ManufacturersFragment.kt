package com.cmdv.feature_main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cmdv.common.Constants
import com.cmdv.feature_main.R
import com.cmdv.feature_main.databinding.FragmentManufacturersBinding
import com.cmdv.feature_main.ui.adapters.ManufacturerRecyclerViewAdapter
import com.cmdv.feature_main.ui.decorators.GridManufacturerItemDecorator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ManufacturersFragment : Fragment() {

    private lateinit var viewModel: ManufacturersViewModel
    private lateinit var binding: FragmentManufacturersBinding

    private lateinit var manufacturerAdapter: ManufacturerRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManufacturersBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ManufacturersViewModel::class.java)

        viewModel.manufacturersLiveData.observe(viewLifecycleOwner, {
            manufacturerAdapter.setItems(it)
        })
    }

    private fun setupRecyclerView() {
        activity?.let { context ->
            manufacturerAdapter = ManufacturerRecyclerViewAdapter(context)
            binding.recyclerViewManufacturer.apply {
                layoutManager = GridLayoutManager(context, Constants.SPAN_COUNT_ITEM_MANUFACTURER)
                adapter = manufacturerAdapter
                addItemDecoration(
                    GridManufacturerItemDecorator(
                        resources.getDimensionPixelOffset(R.dimen.spacing_manufacturer_item),
                        Constants.SPAN_COUNT_ITEM_MANUFACTURER,
                        false
                    )
                )
            }
        }
    }

}