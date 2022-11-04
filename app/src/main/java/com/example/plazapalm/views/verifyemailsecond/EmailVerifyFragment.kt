package com.example.plazapalm.views.verifyemailsecond

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.EmailVerifyFragmentBinding
import com.example.plazapalm.networkcalls.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EmailVerifyFragment : Fragment(R.layout.email_verify_fragment) {
    @Inject
    lateinit var repository: Repository
    private var binding: EmailVerifyFragmentBinding? = null
    private val viewModel: EmailVerifyVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmailVerifyFragmentBinding.inflate(inflater, container, false)
        getData()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
    }
    private fun getData() {

        viewModel.argsData.set( arguments?.getString("comingFrom"))
        val emailFromForgotScreen = arguments?.getString("email")
        //for get email from previous screen..
        when(arguments?.getString("comingFrom")){
            "Login"->{
                viewModel.email.set(emailFromForgotScreen)
                viewModel.getLoginType.set(viewModel.argsData.get())
            }
            "emailUpdate"->{
                //call Verify Email Update Api here..
                viewModel.validateUpdateEmail.set(viewModel.argsData.get())
            }
          else->
          {
              viewModel.email.set(emailFromForgotScreen)

          }

        }
       /* if (emailFromForgotScreen!=null)
        {
            viewModel.email.set(emailFromForgotScreen)
        }*/
    }
}