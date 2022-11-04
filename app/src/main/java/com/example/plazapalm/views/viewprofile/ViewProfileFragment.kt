package com.example.plazapalm.views.viewprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.ViewProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewProfileFragment : Fragment(R.layout.view_profile_fragment) {
    private var binding: ViewProfileFragmentBinding? = null
    private val viewModel: ViewProfileVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ViewProfileFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
    }
}