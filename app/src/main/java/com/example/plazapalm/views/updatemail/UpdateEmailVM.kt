package com.example.plazapalm.views.updatemail

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.datastore.LOGIN_DATA
import com.example.plazapalm.models.LoginDataModel
import com.example.plazapalm.models.VerifyOtpData
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.*
import com.example.plazapalm.utils.Constants.EMAIL_MUST_VALID
import com.example.plazapalm.utils.Constants.SOMETHING_WRONG
import com.example.plazapalm.validation.ValidatorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UpdateEmailVM @Inject constructor(
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
    private var dataStoreUtil: DataStoreUtil,
    private var pref: PreferenceFile
) : ViewModel() {
    var currentEmail = ObservableField("")
    var newEmail = ObservableField("")
    var token = ObservableField("")
    var emailUpdateType = ObservableField("emailUpdate")

    init {
        token.set(pref.retrieveKey("token"))
        dataStoreUtil.readObject(LOGIN_DATA, LoginDataModel::class.java)
        {
            currentEmail.set(it?.data?.email)
        }
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.constUpdateMail -> {
                CommonMethods.context.hideKeyboard()
            }
            R.id.btnUpdateEmail -> {
                if (CommonMethods.context.isNetworkAvailable()) {
                    if (updateMailValidation()) {
                        //call update email api here..
                        callUpdateEmail(view)
                    }
                }
                else
                {
                    CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                }
                //view.findNavController().navigateUp()
            }
            R.id.ivUpdateMail -> {
                view.findNavController().navigateUp()
            }
        }
    }

    /** Validation **/
    private fun updateMailValidation(): Boolean {
        when {
            newEmail.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.EmailCantEmpty)
                return false
            }
            !ValidatorUtils.isEmailValid(newEmail.get()?.trim().toString()) -> {
                CommonMethods.showToast(CommonMethods.context, EMAIL_MUST_VALID)

                return false
            }
            else -> {
                return true
            }

        }
    }

    /**call Update Email Api..**/
    private fun callUpdateEmail(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.EMAIL, newEmail)
        repository.makeCall(
            apiKey = ApiEnums.UPDATE_EMAIL,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<VerifyOtpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<VerifyOtpData> {
                    return retrofitApi.updateEmail(
                        Authorization = token.get()?.trim().toString(),
                        Email = newEmail.get()?.trim().toString()
                    )
                }

                override fun onResponse(res: Response<VerifyOtpData>) {
                    /*From here we will send type for update email and get it in EmailVerify Fragment using Bundle */
                    val bundle = Bundle()
                    bundle.putString("comingFrom", emailUpdateType.get())
                    view.navigateWithId(R.id.emailVerifyFragment, bundle)
                    CommonMethods.showToast(CommonMethods.context, res.body()?.message.toString())
                    CommonMethods.context.hideKeyboard()
                    view.navigateBack()
                }

                override fun onError(message: String) {
                    super.onError(message)
                    CommonMethods.showToast(CommonMethods.context, SOMETHING_WRONG)
                }
            })
    }
}