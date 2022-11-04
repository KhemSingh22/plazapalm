package com.example.plazapalm.views.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.databinding.ReportFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment(R.layout.report_fragment) {
    private var binding: ReportFragmentBinding? = null
    private var viewModel: ReportVM? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReportFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
        binding?.ivReportBackButton?.setOnClickListener {
            view.findNavController().navigateUp()
        }
        binding?.btnReportSubmit?.setOnClickListener {
            view.navigateWithId(R.id.myProfileFragment)
        }
    }
}