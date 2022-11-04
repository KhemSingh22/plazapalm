package com.example.plazapalm.views.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.WelcomeFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.welcome_fragment) {
    private var binding: WelcomeFragmentBinding? = null
    private val viewModel: WelcomeVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        CommonMethods.statusBar(false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        binding?.vm = viewModel
    }
    private fun getData() {
        if (arguments!=null){
            //val argsData=arguments?.getString("comingFrom")
            viewModel.signupType.set(arguments?.getString("comingFrom"))
        }
    }
}