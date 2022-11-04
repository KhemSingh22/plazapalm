package com.example.plazapalm.views.advancesettings.editfontpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FontsListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FontsListFragment : Fragment(R.layout.fonts_list_fragment) {
    private var binding: FontsListFragmentBinding? = null

    //   private val viewModel: FontsListVM by viewModels()
    private val viewModel: EditFrontPageVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FontsListFragmentBinding.inflate(layoutInflater)
        return binding?.root

    }

}