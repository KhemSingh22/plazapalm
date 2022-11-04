package com.example.plazapalm.views.changepass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.ChangePasswordFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment(R.layout.change_password_fragment) {
    private var binding: ChangePasswordFragmentBinding? = null
    private val viewModel: ChangePassVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = ChangePasswordFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(view)
        binding?.vm = viewModel
    }
    private fun getData(view: View) {
        if (arguments != null) {
            viewModel.argsData.set(arguments?.getString("comingFrom"))
            if (viewModel.argsData != null) {
                binding?.tvSettingsChangePassword?.visibility = View.GONE
                binding?.etChangePass?.visibility = View.GONE
                viewModel.email.set(arguments?.getString("email"))
            }
            else
            {
                binding?.tvSettingsChangePassword?.visibility = View.VISIBLE
                binding?.etChangePass?.visibility = View.VISIBLE
                viewModel.email.set(arguments?.getString("email"))
            }

        }
    }
}