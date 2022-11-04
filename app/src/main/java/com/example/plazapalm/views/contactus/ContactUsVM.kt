package com.example.plazapalm.views.contactus

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.models.ContactUsResponseModel
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.*
import com.example.plazapalm.utils.Constants.MESSAGE_CANT_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ContactUsVM @Inject constructor(
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
    private var pref: PreferenceFile
) : ViewModel() {
    var email = ObservableField("")
    var subject = ObservableField("")
    var message = ObservableField("")
    var token = ObservableField("")

    init {
        token.set(pref.retrieveKey("token"))
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.tbContactUs -> {
                view.findNavController().navigateUp()
            }
            R.id.clContactUs -> {

                CommonMethods.context.hideKeyboard()
            }

            /*  R.id.btnContactUsSave -> {
                  view.findNavController().navigateUp()
              }*/

            R.id.btnContactUsSave -> {
                //call api here..
                if (CommonMethods.context.isNetworkAvailable()) {
                    if (contactUsValidation()) {
                        contactUs(view)
                    }
                } else {
                    CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                }
            }

        }
    }

    /**Contact Us Api..**/
    fun contactUs(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put("email", email.get())
        body.put("subject", subject.get())
        body.put("message", message.get())
        repository.makeCall(
            apiKey = ApiEnums.CONTACT_US,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<ContactUsResponseModel>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<ContactUsResponseModel> {
                    return retrofitApi.contactUs(
                        Authorization = token.get().toString(),
                        Email = email.get().toString(),
                        Subject = subject.get().toString(),
                        Message = message.get().toString()
                    )
                }

                override fun onResponse(res: Response<ContactUsResponseModel>) {
                    CommonMethods.showToast(CommonMethods.context, res.body()?.message.toString())
                    view.navigateWithId(R.id.myProfileFragment)
                    // view.navigateBack()
                }
            }
        )
    }

    /** Validation **/
    private fun contactUsValidation(): Boolean {
        when {
            email.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.EmailCantEmpty)
                return false
            }
            subject.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.SUBJECT_CANT_EMPTY)
                return false
            }
            message.get()?.trim()?.isEmpty()!! -> {
                CommonMethods.showToast(CommonMethods.context, MESSAGE_CANT_EMPTY)
                return false
            }
            else -> {
                return true
            }
        }
    }
}