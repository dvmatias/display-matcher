package com.cmdv.feature.ui.fragments.manufacturers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.cmdv.common.utils.Constants
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.cmdv.feature.R
import com.cmdv.feature.databinding.FragmentManufacturersBinding
import com.cmdv.feature.ui.adapters.ManufacturerRecyclerViewAdapter
import com.cmdv.feature.ui.decorators.GridManufacturerItemDecorator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ManufacturersFragment : Fragment() {

    private lateinit var viewModel: ManufacturersFragmentViewModel
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
        viewModel = ViewModelProvider(this).get(ManufacturersFragmentViewModel::class.java)

        viewModel.manufacturersLiveData.observe(viewLifecycleOwner, { statusWrapper ->
            when (statusWrapper.status) {
                LiveDataStatusWrapper.Status.LOADING -> setLoadingStateView()
                LiveDataStatusWrapper.Status.SUCCESS -> statusWrapper.data?.run { setInfoStateView(this) }
                LiveDataStatusWrapper.Status.ERROR -> setErrorStateView()
            }
        })
    }

    private fun setupRecyclerView() {
        activity?.let { context ->
            manufacturerAdapter = ManufacturerRecyclerViewAdapter(context, ::onManufacturerClick)
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

    private fun onManufacturerClick(id: String, name: String) {
        val bundle = bundleOf(
            Constants.ARG_MANUFACTURER_ID_KEY to id,
            Constants.ARG_MANUFACTURER_KEY to name
        )
        Navigation.findNavController(binding.root)
            .navigate(
                R.id.action_manufacturersFragment_to_modelsFragment,
                bundle
            )
    }

    private fun setLoadingStateView() {
        binding.cardViewManufacturersContainer.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setInfoStateView(manufacturers: List<ManufacturerModel>) {
        binding.cardViewManufacturersContainer.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        manufacturerAdapter.setItems(manufacturers)
    }

    private fun setErrorStateView() {

    }

}