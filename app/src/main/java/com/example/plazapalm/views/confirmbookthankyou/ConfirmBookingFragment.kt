package com.example.plazapalm.views.confirmbookthankyou

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.ConfirmBookingFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConfirmBookingFragment : Fragment(R.layout.confirm_booking_fragment) {
    private var binding:ConfirmBookingFragmentBinding?=null
    private val viewModel:ConfirmBookingVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=ConfirmBookingFragmentBinding.inflate(layoutInflater)

        CommonMethods.statusBar(false)
        return binding?.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm=viewModel
    }

}