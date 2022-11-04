package com.example.plazapalm.views.updatemail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.UpdateEmailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateEmailFragment : Fragment(R.layout.update_email_fragment) {
    private var binding: UpdateEmailFragmentBinding? = null
    private val viewModel: UpdateEmailVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=UpdateEmailFragmentBinding.inflate(layoutInflater)
        currentEmail()
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
    }
    private fun currentEmail() {
        binding?.etUpdateMail?.isClickable = false
        binding?.etUpdateMail?.isFocusable = false
    }
}