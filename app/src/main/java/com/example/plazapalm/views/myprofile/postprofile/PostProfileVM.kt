package com.example.plazapalm.views.myprofile.postprofile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.models.SavePostProfileResponse
import com.example.plazapalm.models.ValidateUserNameResponse
import com.example.plazapalm.models.ViewProfileData
import com.example.plazapalm.networkcalls.ApiEnums
import com.example.plazapalm.networkcalls.ApiProcessor
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.networkcalls.RetrofitApi
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@SuppressLint("NotifyDataSetChanged")
@HiltViewModel
class PostProfileVM @Inject constructor(
    private var repository: Repository,
    var dataStoreUtil: DataStoreUtil,
    var pref: PreferenceFile

) : ViewModel() {
    var firstName = ObservableField("")
    var lastName = ObservableField("")
    var userName = ObservableField("")
    var address = ObservableField("")
    var long = ObservableField("")
    var lat = ObservableField("")
    var expireDate = ObservableField("")
    var profileTitle = ObservableField("")
    var description1 = ObservableField("")
    var description2 = ObservableField("")
    var description3 = ObservableField("")
    var token = ObservableField("")
    var categeory = ObservableField("")
    var c_id = ObservableField("")
     var datePicker: DatePickerHelper?=null
    var listData = ObservableField("")
    var photoList: ArrayList<String>? = null

    //    var photoList: ArrayList<AddPhoto>? = null
    var imagesList = ArrayList<AddPhoto>()
    var location = ObservableField("")
    var isClicked: ObservableBoolean = ObservableBoolean(false)

    init {


        /*dataStoreUtil.readObject(LOGIN_DATA, LoginDataModel::class.java) {
           // firstName.set(it?.data?.first_name.toString())
         //   lastName.set(it?.data?.last_name.toString())
            userName.set(it?.data?.user_name.toString())
            profileTitle.set(it?.data?.profileTitle.toString())
            address.set(it?.data?.address.toString())
        }

        dataStoreUtil.readObject(PROFILE_DATA, GetProfileResponseModel::class.java) {
            firstName.set(it?.data?.first_name.toString())
            lastName.set(it?.data?.last_name.toString())

        }*/

    }

    init {
        token.set(pref.retrieveKey("token"))

    }

    fun onTextChange(editable: Editable) {
        if (editable.length > 4) {
            Handler().postDelayed({

                uservalidation(editable)
            }, 1000)
        }

        Log.e("QQWQWQw", editable.toString())
    }

    private fun uservalidation(editable: Editable) {

        repository.makeCall(
            ApiEnums.VALIDATE_USERNAME,
            loader = false,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<ValidateUserNameResponse>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<ValidateUserNameResponse> {
                    return retrofitApi.validateUserName(token.get().toString(), editable.toString())
                }

                override fun onResponse(res: Response<ValidateUserNameResponse>) {
                    if (res.isSuccessful) {
                        if (res.body() != null) {
                            if (res.body()!!.status == 200) {
                                Log.e("QQQQ", res.body()!!.message)
                            } else if (res.body()!!.status == 401) {
                                userName.set("")
                                CommonMethods.showToast(
                                    CommonMethods.context,
                                    res.body()?.message.toString()
                                )
                                Log.e("QQQQ", res.body()!!.message)
                            }
                        } else {
                            CommonMethods.showToast(
                                CommonMethods.context,
                                res.body()?.message.toString()
                            )
                        }

                    } else {
                        CommonMethods.showToast(
                            CommonMethods.context,
                            res.body()?.message.toString()
                        )
                    }
                }

                override fun onError(message: String) {
                    super.onError(message)
                    Log.e("sdsdsd3", message)
                    CommonMethods.showToast(CommonMethods.context, message)
                }

            }
        )

    }

    var dialog: Dialog? = null

    //  val addImagesAdapter by lazy { ViewProAddImageAdapter() }
    val addImagesAdapter by lazy { RecyclerAdapter<ViewProfileData>(R.layout.add_images_view_profile) }


    /**Here clicks method implemented **/
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivViewEditProfileBackBtn -> {
                view.findNavController().navigateUp()
            }
            R.id.clViewEditProfileAdd -> {
                isClicked.set(true)

                if(photoList==null)
                {
                    photoList=ArrayList<String>()
                }

                var bundle=Bundle()
                  bundle.putStringArrayList("imageList",photoList)
                    view.navigateWithId(R.id.action_viewProfileFragment_to_addPhotosFragment,bundle)
                    Log.e("SDDDDDDDssss","data list is empty ")

            }

            R.id.ivViewEditProfileDotsBtn -> {
                showChooseOptionAccountDialog()
            }
            R.id.etVEditProChooseCategory -> {
                val bundle = Bundle()
                bundle.putString("comingFrom", "postProfile")
                view.navigateWithId(
                    R.id.action_viewProfileFragment_to_selectCategoryFragment,
                    bundle
                )
            }
            R.id.etVEditProTags -> {
                val bundle = Bundle()
                bundle.putString("PostProfile", "postProfile")
                view.navigateWithId(R.id.action_viewProfileFragment_to_addCitiesFragment, bundle)
            }
            R.id.btnUpdateEmail -> {

                if (CommonMethods.context.isNetworkAvailable()) {
                    if (validation()) {
                        SaveProfileApi(view)
                    }
                } else {
                    CommonMethods.showToast(CommonMethods.context, Constants.CHECK_INTERNET)
                }
            }
            R.id.etVEditProExpiryDate -> {

                showDatePickerDialog()

//                val c = Calendar.getInstance()
//                val year = c.get(Calendar.YEAR)
//                val month = c.get(Calendar.MONTH)
//                val day = c.get(Calendar.DAY_OF_MONTH)
//
//
//                val dpd = DatePickerDialog(
//                    CommonMethods.context,
//                    R.style.CalenderViewCustom_,
//                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//
//                        // Display Selected date in textbox
//                        expireDate.set("" + year + "-" + monthOfYear + "-" + dayOfMonth)
//
//                    },
//                    year,
//                    month,
//                    day
//                )
//
//                dpd.show()
            }


            /*  R.id.btnViewEditProfile -> {
                  view.findNavController().navigateUp()
              }*/
        }
    }

    private fun SaveProfileApi(view: View) = viewModelScope.launch {

        val body = JSONObject()
        body.put(Constants.FIRST_NAME, firstName.get())
        body.put(Constants.LAST_NAME, lastName.get())
        body.put(Constants.LONG, long.get())
        body.put(Constants.LAT, lat.get())
        body.put(Constants.EXPIRE_DATE, expireDate.get())
        body.put(Constants.LOCATION, location.get())
        body.put(Constants.ADDRESS, address.get())
//      body.put(Constants.LAT,imagesList.get() )
        body.put(Constants.USER_NAME, userName.get())
        body.put(Constants.TAG, "pp")
        body.put(Constants.DISCRIPTON_1, description1.get())
        body.put(Constants.DISCRIPTON_2, description2.get())
        body.put(Constants.DISCRIPTON_3, description3.get())
        body.put(Constants.PROFILE_TITLE, profileTitle.get())
        body.put(Constants.C_ID, c_id.get())

        val logintoken = token.set(pref.retrieveKey("token"))
        var firatname: RequestBody? = null
        var lastname: RequestBody? = null
        var longi: RequestBody? = null
        var lati: RequestBody? = null
        var addresss: RequestBody? = null
        var locationn: RequestBody? = null
        var expiredate: RequestBody? = null
        var username: RequestBody? = null
        var profiletitle: RequestBody? = null
        var desc1: RequestBody? = null
        var desc2: RequestBody? = null
        var desc3: RequestBody? = null
        var tokennn: RequestBody? = null
        var tags: RequestBody? = null
        var c_idd: RequestBody? = null
        var url: RequestBody? = null

        Log.e(
            "sdsdsd",
            firstName.get().toString() + " TTKKDKDKK " + lastName.get()
                .toString() + " TTKKDKDKK " + long.get()!!.toString() + " TTKKDKDKK "
                    + lat.get()!!.toString() + " TTKKDKDKK " + address.get()
                .toString() + " TTKKDKDKK " + address.get()
                .toString() + " TTKKDKDKK " + expireDate.get().toString() + " TTKKDKDKK " +
                    location.get().toString() + " TTKKDKDKK " + expireDate.get()
                .toString() + " TTKKDKDKK " + userName.get()
                .toString() + " TTKKDKDKK " + profileTitle.get()
                .toString() + " TTKKDKDKK " + description1.get().toString() +
                    " TTKKDKDKK " + description1.get().toString() + " TTKKDKDKK " +
                    token.get().toString() + " TTKKDKDKK " + c_id.get().toString()
        )

        try {

            firatname = create("text/plain".toMediaTypeOrNull(), firstName.get().toString())
            lastname = create("text/plain".toMediaTypeOrNull(), lastName.get().toString())
            longi = create("text/plain".toMediaTypeOrNull(), long.get()!!.toString())
            lati = create("text/plain".toMediaTypeOrNull(), lat.get()!!.toString())
            addresss = create("text/plain".toMediaTypeOrNull(), address.get().toString())
            locationn = create("text/plain".toMediaTypeOrNull(), location.get().toString())
            expiredate = create("text/plain".toMediaTypeOrNull(), expireDate.get().toString())
            username = create("text/plain".toMediaTypeOrNull(), userName.get().toString())
            profiletitle = create("text/plain".toMediaTypeOrNull(), profileTitle.get().toString())
            desc1 = create("text/plain".toMediaTypeOrNull(), description1.get().toString())
            desc2 = create("text/plain".toMediaTypeOrNull(), description2.get().toString())
            desc3 = create("text/plain".toMediaTypeOrNull(), description3.get().toString())
            tokennn = create("text/plain".toMediaTypeOrNull(), token.get().toString())
            tags = create("text/plain".toMediaTypeOrNull(), "kk")
            c_idd = create("text/plain".toMediaTypeOrNull(), c_id.get().toString())

            Log.e("sssssaaa", tokennn.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        var surveyImagesParts: Array<MultipartBody.Part?>? = null
        /// Implement imageslist
        //   photoList = ArrayList<AddPhoto>()
        if (photoList!!.size > 0) {
            surveyImagesParts = arrayOfNulls<MultipartBody.Part>(photoList?.size!!)
            for (index in 0 until photoList!!.size) {
                val file = File(
                    photoList!!
                        .get(index)
                        .toString()
                    //  .Image!!
                )

                val surveyBody: RequestBody = RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    file
                )
                surveyImagesParts[index] = createFormData("image_url", file.name, surveyBody)

            }
        }

        repository.makeCall(
            ApiEnums.SAVE_POST_PROFILE,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<SavePostProfileResponse>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<SavePostProfileResponse> {
                    return retrofitApi.postProfile(
                        token.get().toString(),
                        firatname,
                        lastname,
                        longi,
                        expiredate,
                        addresss,
                        locationn,
                        surveyImagesParts!! /*url*/,
                        username,
                        tags,
                        desc1,
                        desc2,
                        desc3,
                        lati,
                        profiletitle,
                        c_idd
                    )

                }

                override fun onResponse(res: Response<SavePostProfileResponse>) {
                    Log.e("sdsdsd0", res.message())

                    if (res.isSuccessful) {
                        if (res.code() == 200) {
                            if (res.body()?.status == 200) {
                                CommonMethods.showToast(
                                    CommonMethods.context,
                                    res.body()?.message.toString()
                                )
                                view.navigateBack()
                                pref.storeBoolKey(Constants.POSTSTATUS, true)

                            } else {
                                Log.e("sdsdsd1", res.message())

                                CommonMethods.showToast(
                                    CommonMethods.context,
                                    res.body()?.message.toString()
                                )
                            }
                        }

                    } else {
                        CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")
                        Log.e("sdsdsd2", res.message())
                    }

                }

                override fun onError(message: String) {
                    super.onError(message)
                    Log.e("sdsdsd3", message)
                    CommonMethods.showToast(CommonMethods.context, message)
                }

            }

        )

    }

    /**Choose Options Dialog (Edit profile, Delete,cancel) **/
    private fun showChooseOptionAccountDialog() {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.choose_profile_options)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            /**choose options click(Button) **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptionTitle)?.setOnClickListener {
                dialog?.dismiss()
            }
            /**Edit Profile Click (Button)**/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptEditProfile)
                ?.setOnClickListener {
                    dialog?.dismiss()
                }
            /** Delete Profile (Button) **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptDeleteProfile)
                ?.setOnClickListener {
                    dialog?.dismiss()
                }
            /** Cancel Click (Button) **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptCancel)?.setOnClickListener {
                dialog?.dismiss()
            }
        }
        dialog?.show()
    }

    fun validation(): Boolean {
        when {
            photoList == null || (photoList!!.size < 3) -> {
                CommonMethods.showToast(CommonMethods.context, Constants.ImageCantEmpty)
                return false
            }
            firstName.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.FirstNameCantEmpty)
                return false
            }

            lastName.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.LastNameCantEmpty)
                return false
            }
            lastName.get()!!.length < 3 -> {
                CommonMethods.showToast(CommonMethods.context, Constants.Description3minimum)
                return false
            }
            userName.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.UserCantEmpty)
                return false
            }

            /* !ValidatorUtils.isEmailValid(email.get()!!) -> {
                 CommonMethods.showToast(CommonMethods.context, "Please Enter Valid Email ")
                 return false
             }*/
            profileTitle.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.ProfileCantEmpty)
                return false
            }
            address.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.AddaddressCantEmpty)
                return false
            }
            location.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.LocationCantEmpty)
                return false
            }

            description1.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.Description1CantEmpty)
                return false
            }
            description1.get()!!.length < 20 -> {
                CommonMethods.showToast(CommonMethods.context, Constants.Description3minimum)
                return false
            }
            description2.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.Description2CantEmpty)
                return false
            }
            description2.get()!!.length < 20 -> {
                CommonMethods.showToast(CommonMethods.context, Constants.Description3minimum)
                return false
            }

            description3.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.Description3CantEmpty)
                return false
            }
            description3.get()!!.length < 20 -> {
                CommonMethods.showToast(CommonMethods.context, Constants.Description3minimum)
                return false
            }
            expireDate.get()?.trim().toString().isEmpty() -> {
                CommonMethods.showToast(CommonMethods.context, Constants.ExpireDateCantEmpty)
                return false
            }


            else -> {
                return true
            }
        }
    }

    private fun showDatePickerDialog() {
        datePicker = DatePickerHelper(CommonMethods.context,true)
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker!!.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
//                tvDate.text = "${dayStr}-${monthStr}-${year}"
                expireDate.set("" + year + "-" + monthStr + "-" + dayStr)
                Log.e("SDSDSDSDSDSDSDSd",""+ year + "-" + monthStr + "-" + dayStr)
            }
        })
    }
}