package com.example.plazapalm.views.advancesettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.AdvanceSettingFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdvanceSettingFragment : Fragment(R.layout.advance_setting_fragment) {
    private var binding: AdvanceSettingFragmentBinding? = null
    private val viewModel: AdvanceSettingVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AdvanceSettingFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
    }
    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(false)
    }
}