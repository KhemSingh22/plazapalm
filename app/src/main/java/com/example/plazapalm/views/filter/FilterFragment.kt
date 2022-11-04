package com.example.plazapalm.views.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FilterFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : Fragment(R.layout.filter_fragment) {
    lateinit var binding: FilterFragmentBinding
    var sliderTextMaxValue = 100
    private val viewModel: FilterFragmentVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilterFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        setCategoryAdapter()
        sliderCustomize()
        // sliderCustomize()
    }

    private fun setCategoryAdapter() {
        binding.rvFilterCategory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFilterCategory.adapter = viewModel.filterAdapter
        binding.rvFilterCategory.adapter?.notifyDataSetChanged()
    }

    private fun sliderCustomize() {
        binding.sliderFilter.addOnChangeListener { _, value, _ ->

            binding.tvFilterMilesValue.text = "${value.toInt()} Miles"
            // binding.tvFilterMilesValue.text = viewModel.miles.set(value.toInt().toString()).toString()
        }
    }
}