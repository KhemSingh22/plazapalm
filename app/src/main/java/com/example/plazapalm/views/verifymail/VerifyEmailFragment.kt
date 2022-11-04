package com.example.plazapalm.views.verifymail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.databinding.VerifyEmailFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailFragment : Fragment(R.layout.verify_email_fragment) {
    private var binding: VerifyEmailFragmentBinding? = null
    private val viewModel: VerifyEmailVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VerifyEmailFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSignupData(view)
        binding?.vm = viewModel
    }


    /**Get email from previous screen and send in verify and resend otp api.. (Signup Screen) **/
    private fun getSignupData(view: View) {
        if (arguments != null) {
            val getEmail = arguments?.getString("email")
            when (arguments?.getString("comingFrom")) {
                "login" -> {
                    if (arguments?.containsKey("email") == true) {
                        viewModel.email.set(getEmail.toString())
                        viewModel.loginType.set(arguments?.getString("comingFrom"))
                    }

                }
                //chooseCategory means comingFrom Signup..
                "signup" -> {
                    if (arguments?.containsKey("email")!!) {
                        val bundle = Bundle()
                        viewModel.email.set(getEmail.toString())
                        bundle.putString("comingFrom", "signup")
                        viewModel.signUpType.set("signup")
                    }

                }
                else -> {
                }
            }
        }

/*
        if (arguments != null) {
            val singupData = arguments?.getString("comingFrom")
          //  singupData=="from"
            if (singupData != null && singupData == "email" && singupData.contains("email")) {
                viewModel.email.set(singupData)
            }
            else{

            }
        }
*/

    }
}