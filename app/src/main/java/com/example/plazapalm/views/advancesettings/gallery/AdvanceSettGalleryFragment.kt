package com.example.plazapalm.views.advancesettings.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.AdvanceSettGalleryFramentFragmentBinding
import com.example.plazapalm.databinding.AdvanceSettingFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdvanceSettGalleryFragment : Fragment(R.layout.advance_sett_gallery_frament_fragment) {
    private var binding:AdvanceSettGalleryFramentFragmentBinding?=null
    private val viewModel:AdvanceSettingGalleryVM by  viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=AdvanceSettGalleryFramentFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm=viewModel
    }

}