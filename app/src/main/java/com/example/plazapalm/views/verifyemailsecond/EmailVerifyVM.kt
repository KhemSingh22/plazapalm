package com.example.plazapalm.views.verifyemailsecond

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plazapalm.R
import com.example.plazapalm.models.VerifyOtpData
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EmailVerifyVM @Inject constructor(
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
    private var pref: PreferenceFile
) : ViewModel() {
    var bundle = Bundle()
    var getLoginType = ObservableField("Login")
    var email = ObservableField("")
    var otp = ObservableField("")
    var argsData = ObservableField("")
    var auth = ObservableField("")
    var validateUpdateEmail = ObservableField("")


    init {
        //get Token from stored data..
        auth.set(pref.retrieveKey("token"))
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.clEmailVerifyMain -> {
                CommonMethods.context.hideKeyboard()
            }

            R.id.btnEmailVerify -> {
                //  CommonMethods.context.hideKeyboard()
                when (argsData.get()) {
                    "Login" -> {
                        //call verifyOtp api here..
                        if (CommonMethods.context.isNetworkAvailable()) {
                            if (validation()) {
                                callVerifyOtpApi(view)
                            }
                        } else {
                            CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                        }
                    }
                    "updateEmail" -> {
                        if (argsData.get() == "updateEmail") {
                            //call update email verify api here..
                            if (CommonMethods.context.isNetworkAvailable()) {
                                if (validation()) {
                                    validateOtpForEmailUpdate()
                                } else {
                                    CommonMethods.showToast(
                                        CommonMethods.context,
                                        Constants.CHECK_INTERNET
                                    )
                                }
                            }
                        } else {

                        }
                    }
                }

            }

            R.id.tvEmailVerifyResend -> {
                if (CommonMethods.context.isNetworkAvailable()) {
                    //call resend api here..
                    reSendOtp(view)
                } else {
                    CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                }
            }
        }
    }

    fun validation(): Boolean {
        when {
            otp.get()?.trim()?.isEmpty()!! -> {
                CommonMethods.showToast(CommonMethods.context, Constants.ENTER_OTP)
                return false
            }
            else -> {
                return true
            }
        }
    }

    /** Verify Otp ApI..**/
    /**Resend Otp Api **/
    private fun reSendOtp(view: View) = viewModelScope.launch {
        val body = JSONObject()
        //body.put(Constants.EMAIL, email)
        body.put(Constants.EMAIL, email)
        repository.makeCall(
            apiKey = ApiEnums.RESEND_OTP,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.reSendOtp(email.get()?.trim().toString())
                }

                override fun onResponse(res: Response<VerifyOtpData>) {
                    CommonMethods.context.hideKeyboard()
                    CommonMethods.showToast(CommonMethods.context, res.body()?.message.toString())
                }
            })
    }

    private fun callVerifyOtpApi(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.EMAIL, email.get())
        body.put(Constants.OTP, otp.get())
        repository.makeCall(
            apiKey = ApiEnums.VERIFY_OTP,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.verifyOtp(
                        email.get()?.trim().toString(),
                        otp.get()?.trim()!!
                    )
                }

                override fun onResponse(res: Response<VerifyOtpData>) {
                    CommonMethods.context.hideKeyboard()
                    val bundle = Bundle()
                    bundle.putString("comingFrom", getLoginType.get())
                    bundle.putString("email", email.get())
                    view.navigateWithId(R.id.changePasswordFragment, bundle)
                    CommonMethods.showToast(CommonMethods.context, res.body()?.message.toString())
                }
            }
        )
    }

    /** call Update email verify api **/
    private fun validateOtpForEmailUpdate() = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.AUTHORIZATION, auth.get())
        body.put(Constants.EMAIL, email.get())
        body.put("Otp", otp.get())
        repository.makeCall(
            apiKey = ApiEnums.VALIDATE_OTP_UPDATE_EMAIL,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.validateOtpUpdateEmail(
                        Authorization = auth.get()?.trim().toString(),
                        Email = email.get()?.trim().toString(),
                        Otp = otp.get()?.trim().toString()
                    )
                }

                override fun onResponse(res: Response<VerifyOtpData>) {
                    CommonMethods.showToast(CommonMethods.context, res.body()?.message.toString())
                }
            }
        )

    }

}