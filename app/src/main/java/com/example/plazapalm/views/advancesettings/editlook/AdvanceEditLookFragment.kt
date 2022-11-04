package com.example.plazapalm.views.advancesettings.editlook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.AdvanceEditLookFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdvanceEditLookFragment : Fragment(R.layout.advance_edit_look_fragment) {

    private var binding:AdvanceEditLookFragmentBinding?=null
    private val viewModel:AdvanceEditLookVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AdvanceEditLookFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm=viewModel
    }

}