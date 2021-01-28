package com.cmdv.feature_main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cmdv.feature_main.databinding.FragmentBrandsBinding
import com.cmdv.feature_main.ui.adapters.BrandRecyclerViewAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class BrandsFragment : Fragment() {

    private lateinit var viewModel: BrandsViewModel
    private lateinit var binding: FragmentBrandsBinding

    private lateinit var brandAdapter: BrandRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrandsBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BrandsViewModel::class.java)

        viewModel.brandsLiveData.observe(viewLifecycleOwner, {
            brandAdapter.setItems(it)
        })
    }

    private fun setupRecyclerView() {
        activity?.let { context ->
            brandAdapter = BrandRecyclerViewAdapter(context)
            binding.recyclerViewBrand.apply {
                layoutManager = GridLayoutManager(context, 4)
                adapter = brandAdapter
            }
        }
    }

}