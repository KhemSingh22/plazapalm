package com.example.plazapalm.views.changepass

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.models.VerifyOtpData
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.*
import com.example.plazapalm.utils.Constants.CONFIRM_PASSWORD_CANT_EMPTY
import com.example.plazapalm.utils.Constants.CURRENT_PASSWORD_CANT_EMPTY
import com.example.plazapalm.utils.Constants.NEW_PASSWORD_CANT_EMPTY
import com.example.plazapalm.utils.Constants.PASSWORD_NOT_MATCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangePassVM @Inject constructor(
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
    private var preferences: PreferenceFile,
    private var dataStore: DataStoreUtil
) : ViewModel() {
    var confirmPassword = ObservableField("")
    var newPassword = ObservableField("")
    var currentPassword = ObservableField("")
    var argsData = ObservableField("")
    var email = ObservableField("")
    var token = ObservableField("")
    var password = ObservableField("")

    init {
        CoroutineScope(Dispatchers.Default).launch {
            /*get token form preference data store..*/
            token.set(preferences.retrieveKey("token"))
        }
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivChangePassword -> {
                view.findNavController().navigateUp()
            }
            R.id.clChangePassMain -> {
                CommonMethods.context.hideKeyboard()
            }
            R.id.btnChangePassword -> {
                CommonMethods.context.hideKeyboard()
                //call change password api here..
                if (argsData.get() == "Login") {
                    //call resent password api..
                    if (CommonMethods.context.isNetworkAvailable()) {
                        if (validation()) {
                            resetPassword(view)
                        }
                    } else {
                        CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                    }
                } else {
                    //Call Change Password Api Here..
                    if (CommonMethods.context.isNetworkAvailable()) {
                        if (validation()) {
                            changePassword(view)
                        }
                    } else {
                        CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                    }
                }
            }
        }
    }

    /**Apply Condition if we are using forgot password flow
     * then current password field will be hide
     * **/
    fun validation(): Boolean {
        if (argsData.get() == "Login") {
            when {
                newPassword.get()?.trim().toString().isEmpty() -> {
                    CommonMethods.showToast(CommonMethods.context, NEW_PASSWORD_CANT_EMPTY)
                    return false
                }
                confirmPassword.get()?.trim()?.isEmpty()!! -> {
                    CommonMethods.showToast(CommonMethods.context, CONFIRM_PASSWORD_CANT_EMPTY)
                    return false
                }
                newPassword.get() != confirmPassword.get() -> {
                    CommonMethods.showToast(CommonMethods.context, PASSWORD_NOT_MATCH)
                    return false
                }
                else -> {
                    return true
                }
            }
        } else {
            when {
                currentPassword.get()?.trim().toString().isEmpty() -> {
                    CommonMethods.showToast(CommonMethods.context, CURRENT_PASSWORD_CANT_EMPTY)
                    return true
                }
                newPassword.get()?.trim().toString().isEmpty() -> {
                    CommonMethods.showToast(CommonMethods.context, NEW_PASSWORD_CANT_EMPTY)
                    return false
                }
                confirmPassword.get()?.trim()?.isEmpty()!! -> {
                    CommonMethods.showToast(CommonMethods.context, CONFIRM_PASSWORD_CANT_EMPTY)
                    return false
                }
                newPassword.get() != confirmPassword.get() -> {
                    CommonMethods.showToast(CommonMethods.context, PASSWORD_NOT_MATCH)
                    return false
                }
                else -> {
                    return true
                }
            }

        }
    }

    private fun changePassword(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.CURRENT_PASSWORD, currentPassword)
        body.put(Constants.NEW_PASSWORD, newPassword)
        repository.makeCall(
            apiKey = ApiEnums.CHANGE_PASSWORD,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.changePassword(
                        Authorization = token.get().toString(),
                        currentPassword.get()?.trim().toString(),
                        newPassword.get()?.trim().toString()
                    )
                }

                override fun onResponse(res: Response<VerifyOtpData>) {
                    if (res.isSuccessful && res.code() == 200) {
                        view.navigateBack()
                        CommonMethods.context.hideKeyboard()
                        CommonMethods.showToast(
                            CommonMethods.context,
                            res.body()?.message.toString()
                        )
                    } else {
                        CommonMethods.showToast(CommonMethods.context, Constants.SOMETHING_WRONG)
                    }

                }
            }
        )
    }

    private fun resetPassword(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.EMAIL, email.get())
        body.put(Constants.PASSWORD, newPassword.get())
        repository.makeCall(
            apiKey = ApiEnums.CHANGE_PASSWORD,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.resetPassword(
                        email.get()?.trim().toString(),
                        newPassword.get()?.trim().toString()
                    )
                }
                override fun onResponse(res: Response<VerifyOtpData>) {
                    if (res.isSuccessful && res.code() == 200) {
                        view.navigateWithId(R.id.loginFragment)
                        CommonMethods.context.hideKeyboard()
                        CommonMethods.showToast(
                            CommonMethods.context,
                            res.body()?.message.toString()
                        )
                    }
                    else
                    {
                        CommonMethods.showToast(CommonMethods.context, Constants.SOMETHING_WRONG)
                    }
                }
            }
        )
    }
}