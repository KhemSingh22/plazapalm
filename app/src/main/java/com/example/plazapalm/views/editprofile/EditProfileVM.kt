package com.example.plazapalm.views.editprofile

import android.content.pm.PackageManager
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.datastore.PROFILE_DATA
import com.example.plazapalm.datastore.PROFILE_IMAGE
import com.example.plazapalm.models.GetProfileResponseModel
import com.example.plazapalm.models.UploadMediaResponse
import com.example.plazapalm.networkcalls.ApiEnums
import com.example.plazapalm.networkcalls.ApiProcessor
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.networkcalls.RetrofitApi
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.*
import com.example.plazapalm.utils.CommonMethods.context
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import javax.inject.Inject


@HiltViewModel
class EditProfileVM @Inject constructor(
    var dataStoreUtil: DataStoreUtil,
    var repository: Repository,
    var pref: PreferenceFile,

    ) : ViewModel() {
    lateinit var pm: PackageManager
    var REQUEST_IMAGE_CAPTURE = ObservableInt(1)
    var REQUEST_PERMISSION = ObservableInt(100)
    var imageFile: File? = null
    val REQUEST_PICK_IMAGE = ObservableInt(2)
    var firstName = ObservableField("")
    var lastName = ObservableField("")
    var argsData = ObservableField("")
    var imageUrl = ObservableField("")
    var notification = ObservableBoolean(false)
    var token = ObservableField("")
    var profile_pic = ObservableField("")

    init {
        token.set(pref.retrieveKey("token"))
        var token = ObservableField("")

        getStoreImage()
        myProfileData()
    }

    /** Clicks Implemented Here **/
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivEditProfileBackBtn -> {
                context.hideKeyboard()
                view.findNavController().navigateUp()
            }
            R.id.clEditProfileMain -> {
                context.hideKeyboard()
            }
            R.id.ivEditSettingBtn -> {
                view.navigateWithId(R.id.action_editProfileFragment_to_settingFragment)
            }
            R.id.tvBtnUpdateProfile -> {
                //call update profile...
                context.hideKeyboard()
                if (context.isNetworkAvailable()) {
                    if (validation()) {
                        updateProfile(view)
                    }

                } else {
                    CommonMethods.showToast(context, Constants.CHECK_INTERNET)
                }
            }
        }
    }

    private fun validation(): Boolean {
        when {
            firstName.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(context, Constants.FirstNameCantEmpty)
                return false
            }
            lastName.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(context, Constants.LastNameCantEmpty)
                return false
            }
            else -> {
                return true
            }
        }
    }

    /**Upload Media Profile..**/
    fun uploadImage(file: File) = viewModelScope.launch {
        val requestBody: RequestBody = imageFile!!.asRequestBody("image/*".toMediaTypeOrNull())
        val imageToUpload = createFormData("profile_picture", imageFile?.name, requestBody)
        repository.makeCall(
            apiKey = ApiEnums.UPLOAD_IMAGES,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<UploadMediaResponse>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<UploadMediaResponse> {
                    return retrofitApi.uploadMedia(Authorization = token.get(), imageToUpload)
                }

                override fun onResponse(res: Response<UploadMediaResponse>) {
                    profile_pic.set(res.body()?.data?.get(0)!!)
                }
            })
    }

    /***call Update Profile Api */
    private fun updateProfile(view: View) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.AUTHORIZATION, token.get())
        body.put(Constants.FIRST_NAME, firstName.get())
        body.put(Constants.LAST_NAME, lastName.get())
        body.put(Constants.IMAGE_URL, profile_pic.get())
        body.put(Constants.NOTIFICATION, notification.get())
        repository.makeCall(
            apiKey = ApiEnums.UPDATE_PROFILE,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<GetProfileResponseModel>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<GetProfileResponseModel> {
                    return retrofitApi.updateProfile(
                        Authorization = token.get()?.trim().toString(),
                        FirstName = firstName.get()?.trim().toString(),
                        LastName = lastName.get()?.trim().toString(),
                        ImageUrl = profile_pic.get().toString(),
                        Notification = notification.get()
                    )
                }

                override fun onResponse(res: Response<GetProfileResponseModel>) {
                    if (res.isSuccessful && res.code() == 200) {
                        //CommonMethods.showToast(context, res.body()?.message.toString())
                        CommonMethods.showToast(context, "Profile updated successfully")
                        dataStoreUtil.saveObject(PROFILE_DATA, res.body())
                        dataStoreUtil.saveData(
                            PROFILE_IMAGE,
                            res.body()?.data?.profile_picture.toString()
                        )
                        profile_pic.set(res.body()?.data?.profile_picture)
                        context.hideKeyboard()
                        view.navigateBack()
                    } else {
                        CommonMethods.showToast(context, Constants.SOMETHING_WRONG)
                    }
                }

                override fun onError(message: String) {
                    super.onError(message)
                    CommonMethods.showToast(context, message)
                }
            }
        )
    }


    /*getStored image*/
    fun getStoreImage() {
        dataStoreUtil.readData(PROFILE_IMAGE) {
            profile_pic.set(it)
        }
    }

    /**My Profile Stored Data **/
    private fun myProfileData() {
        dataStoreUtil.readObject(PROFILE_DATA, GetProfileResponseModel::class.java) {
            firstName.set(it?.data?.first_name.toString())
            lastName.set(it?.data?.last_name.toString())
            //profile_pic.set(it?.data?.profile_picture)
            /*  if (it?.data?.profile_picture != null) {
                  profile_pic.set(it.data.profile_picture)
              }*/
        }
    }
}