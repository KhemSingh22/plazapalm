package com.example.plazapalm.views.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.datastore.LOGIN_DATA
import com.example.plazapalm.models.LoginDataModel
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.pref.token
import com.example.plazapalm.utils.*
import com.example.plazapalm.validation.ValidatorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private var repository: Repository,
    private var dataStore: DataStoreUtil,
    private var cacheUtil: CacheUtil,
    private var preferences: PreferenceFile) : ViewModel() {
    var email = ObservableField("")
    var password = ObservableField("")
    var sendTypeLogin = ObservableField("Login")

    fun clicks(view: View) {
        when (view.id) {
            R.id.clMainLogin -> {
                (MainActivity.context.get() as Activity).hideKeyboard()
            }
            R.id.btnLogin -> {
                if (CommonMethods.context.isNetworkAvailable()) {
                    if (loginValidation()) {
                        //call login Api Here..
                        CommonMethods.context.hideKeyboard()
                        loginApi(view)
                    }
                } else {
                    CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                }
            }

            R.id.btnLoginCategories -> {
                view.navigateWithId(R.id.categoriesListFragment)
            }
            R.id.tvLoginWantSignup -> {
                view.navigateWithId(R.id.action_loginFragment_to_signUpFragment)
            }
            R.id.tvLoginForgotPass -> {
                val bundle = Bundle()
                bundle.putString("comingFrom", sendTypeLogin.get())
                bundle.putString("email", email.get())
                view.navigateWithId(R.id.action_loginFragment_to_forgotPasswordFragment, bundle)
            }
            R.id.btnConfirmBooking -> {
                view.navigateWithId(R.id.thankYouFragment)
            }
        }
    }

    private fun loginApi(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.EMAIL, email.get())
        body.put(Constants.PASSWORD, password.get())
        body.put(Constants.DEVICE_TOKEN, Constants.DeviceToken)
        body.put(Constants.DEVICE_TYPE, Constants.DeviceType)
        repository.makeCall(
            apiKey = ApiEnums.LOGIN,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<LoginDataModel>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<LoginDataModel> {
                    return retrofitApi.loginApi(
                        email.get()?.trim().toString(),
                        password.get()?.trim().toString(),
                        Constants.DeviceToken,
                        Constants.DeviceType
                    )
                }
                override fun onResponse(res: Response<LoginDataModel>) {
                    if (res.isSuccessful && res.code() == 200) {
                        if (res.body()?.status == 200) {
                            // dataStore.saveObject(PROFILE_DATA, GetProfileData::class.java)
                            dataStore.saveObject(LOGIN_DATA, res.body())
                            preferences.storeKey(token, res.body()?.data?.token ?: "")
                            Log.e("SDSDSDSDSDSDSDSDs",res.body()?.data?.token.toString())
                            view.navigateWithId(R.id.action_loginFragment_to_dashBoardFragment)
                            CommonMethods.context.hideKeyboard()
                            CommonMethods.showToast(
                                CommonMethods.context,
                                res.body()?.message.toString()
                            )

                        } else {
                            val bundle = Bundle()
                            bundle.putString("comingFrom", "login")
                            //bundle.putString("login", email.get())
                            bundle.putString("email", email.get())
                            view.navigateWithId(R.id.verifyEmailFragment, bundle)
                            CommonMethods.showToast(CommonMethods.context, res.body()?.message.toString())
                            CommonMethods.context.hideKeyboard()

                        }
                    } else {
                        CommonMethods.showToast(
                            CommonMethods.context,
                            res.body()?.message.toString()
                        )
                    }

                }
            }
        )
    }

    /*Validations for login..*/
    private fun loginValidation(): Boolean {
        when {
            email.get()?.trim()?.isEmpty()!! -> {
                CommonMethods.showToast(CommonMethods.context, Constants.EmailCantEmpty)
                return false
            }
            !ValidatorUtils.isEmailValid(email.get()!!.trim()) -> {
                CommonMethods.showToast(CommonMethods.context, Constants.ENTER_VALID_EMAIL)
                return false
            }
            password.get()?.trim()?.isEmpty()!! -> {
                CommonMethods.showToast(CommonMethods.context, Constants.PasswordCantEmpty)
                return false
            }
            else -> {
                return true
            }
        }
    }
}