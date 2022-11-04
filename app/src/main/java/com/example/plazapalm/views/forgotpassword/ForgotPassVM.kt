package com.example.plazapalm.views.forgotpassword

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import com.example.plazapalm.models.VerifyOtpData
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.utils.*
import com.example.plazapalm.validation.ValidatorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ForgotPassVM @Inject constructor(
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
) : ViewModel() {

    var email = ObservableField("")
    var loginArgsType = ObservableField("Login")

    /**Clicks Implemented Here */
    fun onClicks(view: View) {
        when (view.id) {
            R.id.clForgotMain -> {
                (MainActivity.context.get() as Activity).hideKeyboard()
            }
            R.id.ivForgotPassword -> {
                view.findNavController().navigateUp()
                CommonMethods.context.hideKeyboard()
            }

            R.id.constForgotPassword -> {
                (MainActivity.context.get() as Activity).hideKeyboard()
            }
            R.id.btnForgotSendCode -> {
                //Call Forgot Api Here...
               // CommonMethods.showToast(CommonMethods.context, "In Progress ")
                if (CommonMethods.context.isNetworkAvailable()) {
                    if (forgotValidation()) {
                        forgotPassword(view)
                    }
                } else {
                    CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                }
            }
        }
    }
    fun forgotPassword(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.EMAIL, email)
        repository.makeCall(
            apiKey = ApiEnums.FORGOT_PASS,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.forgotPassword(email.get()?.trim().toString())
                }
                override fun onResponse(res: Response<VerifyOtpData>) {
                    //send email to next screen for email requirement using bundle
                    val bundle = Bundle()
                    bundle.putString("comingFrom", loginArgsType.get())
                    bundle.putString("email", email.get())
                    CommonMethods.showToast(CommonMethods.context,res.body()?.message.toString())
                    view.navigateWithId(R.id.action_forgotPasswordFragment_to_emailVerifyFragment, bundle)
                    CommonMethods.context.hideKeyboard()

                }
            })
    }
    private fun forgotValidation(): Boolean {
        when {
            email.get()?.trim()?.isEmpty()!! -> {
                CommonMethods.showToast(CommonMethods.context, Constants.EmailCantEmpty)
                return false
            }
            !ValidatorUtils.isEmailValid(email.get()?.trim()!!.toString()) -> {
                CommonMethods.showToast(CommonMethods.context, Constants.ENTER_VALID_EMAIL)
                return false
            }
            else ->
            {
                return true
            }
        }
    }
}