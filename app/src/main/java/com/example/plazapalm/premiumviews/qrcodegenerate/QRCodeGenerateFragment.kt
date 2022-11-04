package com.example.plazapalm.premiumviews.qrcodegenerate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.QrCodeGenerateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QRCodeGenerateFragment : Fragment(R.layout.qr_code_generate_fragment) {
    private var binding: QrCodeGenerateFragmentBinding? = null
    private val viewModel: QRCodeGenerateVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QrCodeGenerateFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
    }

}