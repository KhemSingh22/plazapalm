package com.example.plazapalm.views.settings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.models.UserLogoutModel
import com.example.plazapalm.models.VerifyOtpData
import com.example.plazapalm.networkcalls.ApiEnums
import com.example.plazapalm.networkcalls.ApiProcessor
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.networkcalls.RetrofitApi
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.CommonMethods.context
import com.example.plazapalm.utils.CommonMethods.deviceToken
import com.example.plazapalm.utils.Constants
import com.example.plazapalm.utils.Constants.DeviceType
import com.example.plazapalm.utils.Constants.SOMETHING_WRONG
import com.example.plazapalm.utils.isNetworkAvailable
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private var dataStoreUtil: DataStoreUtil,
    private var repository: Repository,
    private var pref: PreferenceFile
) : ViewModel() {
    var token = ObservableField("")
    var dialog: Dialog? = null

    init {
        token.set(pref.retrieveKey("token"))
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivSettingBackBtn -> {
                view.findNavController().navigateUp()
            }
            R.id.tvSettingsChangePassword -> {
                view.navigateWithId(R.id.action_settingsFragment_to_changePasswordFragment)
            }
            R.id.tvSettingsUpdateEmail -> {
                view.navigateWithId(R.id.action_settingsFragment_to_updateEmailFragment)
            }
            R.id.tvSettingsContactUs -> {
                view.navigateWithId(R.id.action_settingsFragment_to_contactUsFragment)
            }
            R.id.tvSettingsPrivacyPolicy -> {
                view.navigateWithId(R.id.action_settingsFragment_to_privacyPolicyFragment)
            }
            R.id.tvSettingsTermsUse -> {
                view.navigateWithId(R.id.action_settingsFragment_to_termsUseFragment)
            }
            R.id.tvSettingsLogOut -> {
                //open logout alert
                userLogOutAlert(view)
            }
            R.id.tvSettingsDeleteAccount -> {
                showDeleteAccountDialog(view)
            }
        }
    }

    /**Here Implemented Delete Account Dialog**/
    private fun showDeleteAccountDialog(view: View) {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(context, android.R.style.Theme_Dialog)
            if (dialog?.window != null) {
                dialog?.window?.attributes?.windowAnimations = R.style.popup_window_animation
            }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.delete_account_alert)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            dialog?.findViewById<AppCompatTextView>(R.id.tvDeleteBtn)?.setOnClickListener {
                if (context.isNetworkAvailable()) {
                    deleteAccount(view)
                } else {
                    CommonMethods.showToast(context, Constants.CHECK_INTERNET)
                }
                dialog?.dismiss()
            }
            dialog?.findViewById<AppCompatTextView>(R.id.tvDeleteCancelBtn)?.setOnClickListener {
                dialog?.dismiss()
            }
        }
        dialog?.show()
    }

    /**log-out alert **/
    private fun userLogOutAlert(view: View) {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.logout_alert)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            dialog?.findViewById<AppCompatTextView>(R.id.tvLogOutBtn)?.setOnClickListener {
                if (context.isNetworkAvailable()) {
                    //call logout api here..
                    logOutApi(view)
                } else {
                    CommonMethods.showToast(context, Constants.CHECK_INTERNET)
                }
                dialog?.dismiss()
            }
            dialog?.findViewById<AppCompatTextView>(R.id.tvLogoutCancel)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.show()
        }
    }

    /** Delete Account Api..***/
    private fun deleteAccount(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.AUTHORIZATION, token.get())
        repository.makeCall(
            apiKey = ApiEnums.DELETE_ACCOUNT,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.deleteAccount(Authorization = token.get().toString())
                }

                override fun onResponse(res: Response<VerifyOtpData>) {
                    // CommonMethods.showToast(context, USER_ACCOUNT_DELETED)
                    pref.clearPreference()
                    dataStoreUtil.clearDataStore {
                        it
                    }
                    CommonMethods.showToast(context, res.body()?.message.toString())
                    view.navigateWithId(R.id.loginFragment)
                }
            }
        )
    }

    /*Logout Api.. */
    private fun logOutApi(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put("Authorization", token.get())
        body.put("device_token", deviceToken)
        body.put("device_type", DeviceType)
        repository.makeCall(
            apiKey = ApiEnums.LOG_OUT,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<UserLogoutModel>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<UserLogoutModel> {

                    return retrofitApi.logOutUser(
                        Authorization = token.get()?.trim().toString(),
                        DeviceToken = deviceToken.get(),
                        DeviceType
                    )
                }
                override fun onResponse(res: Response<UserLogoutModel>) {
                    dataStoreUtil.clearDataStore { clear -> }
                    pref.clearPreference()
                    view.findNavController().navigate(R.id.action_setting_to_loginFragment)
                    with(CommonMethods) {
                        showToast(context, res.body()?.message.toString())
                    }
                }
                override fun onError(message: String) {
                    super.onError(message)
                    CommonMethods.showToast(context, SOMETHING_WRONG)
                }
            })
    }
}