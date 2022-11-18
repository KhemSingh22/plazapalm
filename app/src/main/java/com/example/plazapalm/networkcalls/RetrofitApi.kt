package com.example.plazapalm.networkcalls

import com.example.plazapalm.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    /*SignUp api..*/
    @FormUrlEncoded
    @POST(SIGN_UP)
    suspend fun signUp(
        @Field("first_name") FirstName: String?,
        @Field("last_name") LastName: String?,
        @Field("email") Email: String?,
        @Field("password") Password: String?,
        @Field("device_token") DeviceToken: String?,
        @Field("device_type") DeviceType: String?,
    ): Response<SignupResponseModel>

    /*Verify Otp api..*/
    @FormUrlEncoded
    @POST(VERIFY_OTP)
    suspend fun verifyOtp(
        @Field("email") Email: String?,
        @Field("otp") Otp: String?,
    ): Response<VerifyOtpData>

    /*Resend Otp api..*/
    @FormUrlEncoded
    @POST(RESEND_OTP)
    suspend fun reSendOtp(
        @Field("email") Email: String?
    ): Response<VerifyOtpData>

    /*Forgot Password api..*/
    @FormUrlEncoded
    @POST(FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Field("email") Email: String?
    ): Response<VerifyOtpData>


    /*Login Api ..*/
    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun loginApi(
        @Field("email") Email: String? = "",
        @Field("password") Password: String? = "",
        @Field("device_token") DeviceToken: String? = "",
        @Field("device_type") DeviceType: String? = ""
    ): Response<LoginDataModel>

    /*Change Password api.. */
    @FormUrlEncoded
    @POST(CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header("Authorization") Authorization: String,
        @Field("currentPassword") CurrentPassword: String? = "",
        @Field("newPassword") NewPassword: String? = "",
    ): Response<VerifyOtpData>


    /*Contact Us api..*/
    @FormUrlEncoded
    @POST(CONTACT_US)
    suspend fun contactUs(
        @Header("Authorization") Authorization: String,
        @Field("email") Email: String? = "",
        @Field("subject") Subject: String? = "",
        @Field("message") Message: String? = ""
    ): Response<ContactUsResponseModel>


    /*Delete Account api.. */
    @DELETE(DELETE_ACCOUNT)
    suspend fun deleteAccount(
        @Header("Authorization") Authorization: String,
    ): Response<VerifyOtpData>


    /*Update email api..*/
    @FormUrlEncoded
    @POST(UPDATE_EMAIL)
    suspend fun updateEmail(
        @Header("Authorization") Authorization: String,
        @Field("email") Email: String?
    ): Response<VerifyOtpData>

    /*Log out api..*/
    @FormUrlEncoded
    @POST(LOG_OUT)
    suspend fun logOutUser(
        @Header("Authorization") Authorization: String,
        @Field("device_token") DeviceToken: String?,
        @Field("device_type") DeviceType: String?
    ): Response<UserLogoutModel>


    /*Reset Password api...*/
    @FormUrlEncoded
    @POST(RESET_PASSWORD)
    suspend fun resetPassword(
        @Field("email") Email: String?,
        @Field("password") Password: String?
    ): Response<VerifyOtpData>


    /*Get Profile Api.. */
    @GET(GET_PROFILE)
    suspend fun getProfileApi(
        @Header("Authorization") Authorization: String?
    ): Response<GetProfileResponseModel>


    /*Update profile api..*/
    @FormUrlEncoded
    @POST(UPDATE_PROFILE)
    suspend fun updateProfile(
        @Header("Authorization") Authorization: String?,
        @Field("first_name") FirstName: String?,
        @Field("last_name") LastName: String?,
        @Field("image_url") ImageUrl: String?,
        @Field("notification") Notification: Boolean? = false,
    ): Response<GetProfileResponseModel>


    @FormUrlEncoded
    @POST(VALIDATE_OTP_UPDATE_EMAIL)
    suspend fun validateOtpUpdateEmail(
        @Header("Authorization") Authorization: String?,
        @Field("email") Email: String?,
        @Field("otp") Otp: String?,
    ): Response<VerifyOtpData>


    /*Upload media api..*/
    @Multipart
    @POST(UPLOAD_MEDIA)
    suspend fun uploadMedia(
        @Header("Authorization") Authorization: String?,
        @Part images: MultipartBody.Part
    ): Response<UploadMediaResponse>

    /*Get Categories Api....*/
    @GET(GET_CATEGORIES)
    suspend fun getCategories(
        @Header("Authorization") Authorization: String,
        @Query("lat") Lat: Double,
        @Query("long") Long: Double,
        @Query("offset") OffSet: Int,
        @Query("limit") Limit: Int,
        @Query("search") Search: String
    ): Response<CategoriesResponseModel>

    /*   *//*Add to Calendar...*//*
    @FormUrlEncoded
    @POST(ADD_TO_CALENDAR)
    suspend fun addToCalendar(
        @Field("category_name") CategoryName:String,
        @Field("choose_date") ChooseDate:String,
        @Field("post_profile_id") Post_profile_id:String,
        @Field("choose_time") ChooseTime:String,
        @Field("description") Description:String,
        @Field("items[]")
    ):Response<AddToCalendarResponseModel>
*/
    @GET(GET_PREMIUM_STATUS)
    suspend fun isPremiumStatus(
        @Header("Authorization") Authorization: String
    ): Response<CategoriesResponseModel>

    @FormUrlEncoded
    @POST(GET_PROFILE_BY_CATEGORERY)
    suspend fun  getProfileByCategory(
        @Header("Authorization")  Authorization: String,
        @Field("c_id") c_id:String,
        @Field("offset") offset:Int,
        @Field("limit") limit:Int,
        @Field("lat")lat:Double,
        @Field("long")long:Double,
        @Field("search")search:String
    ):Response<GetProfilebyCateResponse>


    @FormUrlEncoded
    @POST(SAVE_POST_PROFILE)
    suspend fun postProfile(
        @Header("Authorization") Authorization: String?,
        @Field("first_name") FirstName: String?,
        @Field("last_name") LastName: String?,
        @Field("long") long: String?,
        @Field("expiry_date") expiry_date: String?,
        @Field("address") address: String?,
        @Field("location_text") location_text: String?,
        @Field ("image_url") image_url: ArrayList<String>,
//        @Part("image_url") image_url : MultipartBody.Part,
        @Field("user_name") user_name: String,
        @Field("tags") tags: String?,
        @Field("description_2") description_2: String?,
        @Field("description_1") description_1: String?,
        @Field("description_3") description_3: String?,
        @Field("lat") lat: String?,
        @Field("profile_title") profile_title: String?,
        @Field("c_id") c_id: String?,
        ): Response<SavePostProfileResponse>



/*
    @Multipart
    @POST(SAVE_POST_PROFILE)
    suspend fun postProfile(

        @Header("Authorization") Authorization: String?,
        @Part("first_name") FirstName: RequestBody?,
        @Part("last_name") LastName: RequestBody?,
        @Part("long") long: RequestBody?,
        @Part("expiry_date") expiry_date: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("location_text") location_text: RequestBody?,
        @Part image_url : Array<MultipartBody.Part?>,
//        @Part("image_url") image_url : MultipartBody.Part,
        @Part("user_name") user_name: RequestBody?,
        @Part("tags") tags: RequestBody?,
        @Part("description_2") description_2: RequestBody?,
        @Part("description_1") description_1: RequestBody?,
        @Part("description_3") description_3: RequestBody?,
        @Part("lat") lat: RequestBody?,
        @Part("profile_title") profile_title: RequestBody?,
        @Part("c_id") c_id: RequestBody?,

        ): Response<SavePostProfileResponse>
*/

    @FormUrlEncoded
    @POST(VALIDATE_USERNAME)
    suspend fun validateUserName(
        @Header("Authorization") Authorization: String?,
        @Field("user_name") user_name: String?,
    ): Response<ValidateUserNameResponse>

    @GET(GET_POST_PROFILE)
    suspend fun getPostProfile(
        @Header("Authorization") Authorization: String,
        @Query("p_id") p_id: String,
        @Query("lat") Lat: Double,
        @Query("long") Long: Double
    ): Response<GetPostProfileResponse>


    @DELETE("postProfileDelete/{name}")
    suspend fun deleteProfilePost(
        @Header("Authorization") Authorization: String,
        @Path("name") name:String

    ): Response<DeletePostProfileResponse>
    /*Upload media api..*/

    @Multipart
    @POST(UPLOAD_MEDIA)
    suspend fun uploadMediaPostProfile(
        @Header("Authorization") Authorization: String?,
        @Part images: Array<MultipartBody.Part?>
    ): Response<UploadMediaResponse>


    @FormUrlEncoded
    @POST(POST_PRPFILE_UPDATE)
    suspend fun UpdatepostProfile(
        @Header("Authorization") Authorization: String?,
        @Field("first_name") FirstName: String?,
        @Field("last_name") LastName: String?,
        @Field("long") long: String?,
        @Field("expiry_date") expiry_date: String?,
        @Field("address") address: String?,
        @Field("location_text") location_text: String?,
        @Field ("image_url") image_url: ArrayList<String>,
//      @Part("image_url") image_url : MultipartBody.Part,
        @Field("user_name") user_name: String,
        @Field("tags") tags: String?,
        @Field("description_2") description_2: String?,
        @Field("description_1") description_1: String?,
        @Field("description_3") description_3: String?,
        @Field("lat") lat: String?,
        @Field("profile_title") profile_title: String?,
        @Field("c_id") c_id: String?,
        @Field("p_id") p_id: String?
        ) : Response<UpdateProfileResponse>

    @FormUrlEncoded
    @POST(ADD_TOFAV_POSTPROFILE)
    suspend fun AddtoFav(
        @Header ("Authorization") Authorization  : String?,
        @Field("p_id") p_id :String?,
        @Field("favType") favType : Boolean?
    ):Response<AddFavPostProfileResponse>

    @GET(GET_FAVOURITES)
    suspend fun GetFavDetals(
        @Header ("Authorization") Authorization: String?,
        @Query("lat") lat :Double,
        @Query("long") long :Double
    ) :Response<GetFavResponse>

    @POST("likesDislikes")
    @FormUrlEncoded
    fun likesDislikes(
        @Header("Authorization") Authorization :String,
        @Field("p_id") p_id :String,
        @Field("likeStatus") likeStatus :Boolean,
        @Field("dislikeStatus") dislikeStatus :Boolean
    ):Response<LikesResPonse>
}