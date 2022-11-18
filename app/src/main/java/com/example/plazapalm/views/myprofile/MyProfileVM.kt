package com.example.plazapalm.views.myprofile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.datastore.PROFILE_DATA
import com.example.plazapalm.datastore.PROFILE_IMAGE
import com.example.plazapalm.models.GetProfileResponseModel
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.Constants
import com.example.plazapalm.utils.hideKeyboard
import com.example.plazapalm.utils.navigateWithId
import com.example.plazapalm.views.myprofile.qrcodescanner.QrCodeScannerFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MyProfileVM @Inject constructor(
    private var dataStoreUtil: DataStoreUtil,
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
    private var pref: PreferenceFile

    ) : ViewModel() {

    var premium : Int? = null
    var loginData = ObservableField("")
    var firstName = ObservableField("first name")
    var storedImageUrl = ObservableField("")
    val qrCodeScanner : QrCodeScannerFragment by lazy { qrCodeScanner }
    var token = ObservableField("")
    var status = ObservableField("Post a Profile ")
    var changestatus = ObservableBoolean(false)

    init {

        token.set(pref.retrieveKey("token"))

        if(pref.retrieveBoolKey(Constants.POSTSTATUS)!!.equals(true)){
            changestatus.set(true)
        }else{
            changestatus.set(false)
        }

        //saveProfileImageUrl()

        getProfileImageUrl()
    }

    /**Click Implementing Here **/
    fun onClick(view: View) {
        when (view.id) {
            R.id.clMain -> {
                CommonMethods.context.hideKeyboard()
            }
            R.id.tvAccount -> {
                view.navigateWithId(R.id.action_myProfileFragment_to_editProfileFragment)
            }
            R.id.tvViewMyProfile -> {
                val bundle = Bundle()

                if (changestatus.get()){

                    bundle.putString("comingFrom","isViewProfile")
                    status.set("View Profile ")
                    view.navigateWithId(R.id.action_myProfileFragment_to_favDetailsFragment,bundle)

                } else {

                    status.set("Post a Profile ")
                    bundle.putString(Constants.FROM_MY_PROFILE,"PostProfile")
                    view.navigateWithId(R.id.action_myProfileFragment_to_viewProfileFragment)

                }
            }

            R.id.tvCalendar -> {
                view.navigateWithId(R.id.action_myProfileFragment_to_calendarFragment)
            }
            R.id.tvFavourite -> {
                view.navigateWithId(R.id.action_myProfileFragment_to_favouritesFragment)
            }

            R.id.tvUpgrade -> {
                view.navigateWithId(R.id.action_myProfileFragment_to_upgradeProFragment)
            }
            R.id.tvAdvanceSetting -> {
                view.navigateWithId(R.id.action_myProfileFragment_to_advanceSettingFragment)
            }

            R.id.tvQRCode -> {
                view.navigateWithId(R.id.action_myProfileFragment_to_QRCodeGenerateFragment)
            }

            //for scanner
            /*  R.id.ivMyProfileScanner -> {
                  view.findViewById(R.id.action_myProfileFragment_to_qrCodeScannerFragment)
              }*/
        }
    }

    /**call Get Profile Api..**/
    fun getProfile() = viewModelScope.launch {
        val body = JSONObject()
        body.put("Authorization", token.get())
        repository.makeCall(
            apiKey = ApiEnums.GET_PROFILE,
            loader = false,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<GetProfileResponseModel>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<GetProfileResponseModel> {
                    return retrofitApi.getProfileApi(Authorization = token.get().toString())
                }
                override fun onResponse(res: Response<GetProfileResponseModel>) {

                    val responseData = res.body()
                    dataStoreUtil.saveObject(PROFILE_DATA, res.body())
                    dataStoreUtil.saveData(PROFILE_IMAGE,
                        res.body()?.data?.profile_picture.toString())
                    storedImageUrl.set(res.body()?.data?.profile_picture)

                    Log.e("QWQWQWQW",responseData.toString())
                    // myProfileData()

                }

                override fun onError(message: String) {
                    super.onError(message)
                    CommonMethods.showToast(CommonMethods.context, Constants.SOMETHING_WRONG)
                }
            }
        )
    }


    private fun getProfileImageUrl() {
        dataStoreUtil.readData(PROFILE_IMAGE) {
            storedImageUrl.set(it)
        }
    }

    fun myProfileData() {
        dataStoreUtil.readObject(PROFILE_DATA, GetProfileResponseModel::class.java) {
            firstName.set(it?.data?.first_name.toString() + " " + " " + it?.data?.last_name.toString())

             storedImageUrl.set(it?.data?.profile_picture)

            /* if (it?.data?.profile_picture != null) {
                storedImageUrl.set(it.data.profile_picture)
             }*/
        }
    }
}


