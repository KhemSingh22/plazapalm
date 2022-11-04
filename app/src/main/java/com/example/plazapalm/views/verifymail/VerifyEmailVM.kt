package com.example.plazapalm.views.verifymail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plazapalm.R
import com.example.plazapalm.models.VerifyOtpData
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.*
import com.example.plazapalm.utils.Constants.CHECK_INTERNET
import com.example.plazapalm.utils.Constants.ENTER_OTP
import com.example.plazapalm.utils.Constants.OTP
import com.example.plazapalm.utils.Constants.OTP_RESENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VerifyEmailVM @Inject constructor(
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
    var pref:PreferenceFile
) : ViewModel() {
    var otp = ObservableField("")
    var email = ObservableField("")
    var signUpType=ObservableField("")
    var loginType=ObservableField("")
    fun onClicks(view: View) {
        when (view.id) {
            R.id.constMain -> {
                CommonMethods.context.hideKeyboard()
            }
            R.id.ivVerifyEmailBackBtn -> {
                CommonMethods.context.hideKeyboard()
                view.navigateBack()
            }
            R.id.btnVerifyEmail -> {
                if (CommonMethods.context.isNetworkAvailable()) {
                    if (otpValidation()) {
                        //call Otp verify api
                        verifyOtp(view)
                    }
                }
                else
                {
                    CommonMethods.showToast(CommonMethods.context, CHECK_INTERNET)
                }
            }
            R.id.tvVerifyEmailResendOtp -> {
                if (CommonMethods.context.isNetworkAvailable()) {
                    //Call Resend Api..
                    reSendOtp()
                } else {
                    CommonMethods.showToast(CommonMethods.context, CHECK_INTERNET)
                }
            }
        }
    }
    private fun verifyOtp(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.EMAIL, email.get())
        body.put(OTP, otp.get())
        repository.makeCall(
            apiKey = ApiEnums.VERIFY_OTP,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.verifyOtp(
                        email.get()?.trim().toString(),
                        otp.get()?.trim()
                    )
                }
                override fun onResponse(res: Response<VerifyOtpData>) {
                    CommonMethods.context.hideKeyboard()
                    val bundle=Bundle()
                   // bundle.putString("comingFromLogin",loginType.get())
                    bundle.putString("comingFrom",signUpType.get())
                    pref.storeKey("token",res.body()?.data?.token.toString())
                    view.navigateWithId(R.id.action_verifyEmailFragment_to_welcomeFragment,bundle)
                    CommonMethods.showToast(CommonMethods.context,res.body()?.message.toString())
                    Log.e("sdsdsds",res.body()?.message.toString())

                }

                override fun onError(message: String) {
                    super.onError(message)
                    CommonMethods.showToast(CommonMethods.context, message.toString())
                     Log.e("POPP",message.toString())
                }
            })
    }

    private fun otpValidation(): Boolean {
        when {
            otp.get()?.trim().isNullOrEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, ENTER_OTP)
                return false
            }
            else -> {
                return true
            }
        }
    }

     private fun reSendOtp() = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.EMAIL, email.get())
        repository.makeCall(
            apiKey = ApiEnums.RESEND_OTP,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.reSendOtp(email.get())
                }
                override fun onResponse(res: Response<VerifyOtpData>) {
                    CommonMethods.showToast(CommonMethods.context,res.body()?.message.toString())
                    //CommonMethods.showToast(CommonMethods.context, OTP_RESENT)
                }
            }
        )
    }
}