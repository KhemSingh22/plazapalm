package com.example.plazapalm.views.forgotpassword

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import com.example.plazapalm.databinding.ForgotPasswordFragmentBinding
import com.example.plazapalm.utils.AppController
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.hideKeyboard
import com.example.plazapalm.utils.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {

    var binding: ForgotPasswordFragmentBinding? = null
    private val viewmodel: ForgotPassVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ForgotPasswordFragmentBinding.inflate(inflater, container, false)


        getArgsLoginData()
      /*  if (arguments?.get("comin gFrom")!=null && arguments?.get("comingFrom")=="verify"){
        }*/
        CommonMethods.context.hideKeyboard()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewmodel
    }
    override fun onResume() {
        super.onResume()
       CommonMethods.statusBar(true)

    }
    private fun getArgsLoginData(){
        if(arguments!=null){
            val getLoginData=arguments?.getString("comingFrom")
            if(getLoginData!=null && getLoginData == viewmodel.loginArgsType.get()){
            }
        }
    }
}