package com.example.plazapalm.premiumviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.PremiumEditProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PremiumEditProfileFragment : Fragment(R.layout.premium_edit_profile_fragment) {
    lateinit var binding:PremiumEditProfileFragmentBinding
    private val viewModel: PremiumEditProVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=PremiumEditProfileFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm=viewModel
    }

}