package com.example.plazapalm.views.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.SignupFragmentBinding
import com.example.plazapalm.utils.AppController
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.views.verifymail.VerifyEmailVM_Factory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.signup_fragment) {
    private var binding:SignupFragmentBinding?=null
    private val viewModel:SignupVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=SignupFragmentBinding.inflate(layoutInflater)
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