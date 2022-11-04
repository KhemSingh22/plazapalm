package com.example.plazapalm.views.reportselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FragmentReportChooseOptionBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportChooseOptionFragment : Fragment(R.layout.fragment_report_choose_option) {
    private var binding: FragmentReportChooseOptionBinding? = null
    private val viewModel: ReportChooseVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportChooseOptionBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
        setAdapter()
        // setItemSingleSelection()
    }

    private fun setAdapter() {
        binding?.rvReportOptions?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvReportOptions?.adapter = viewModel.reportChooseAdapter
        binding?.rvReportOptions?.adapter?.notifyDataSetChanged()
    }


    private fun setItemSingleSelection() {
        viewModel.isChecked
        viewModel.reportChooseAdapter.setOnItemClick { view, position, type ->
            when (type) {
                "onReportChooseItemClick" -> {
                    viewModel.reportModelList.map {
                        if (viewModel.reportModelList[position].isChecked) {
                            // viewModel.isChecked.set(false)
                            viewModel.isChecked.set(true)
                        } else {
                            viewModel.isChecked.set(false)
                        }
                    }
                }
            }
        }
    }
}