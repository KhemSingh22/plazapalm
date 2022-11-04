package com.example.plazapalm.views.bookingdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.BookingDetailsFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookingDetailsFragment : Fragment(R.layout.booking_details_fragment) {
    private var binding: BookingDetailsFragmentBinding? = null
    private val viewModel: BookingDetailsVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BookingDetailsFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
    }

}