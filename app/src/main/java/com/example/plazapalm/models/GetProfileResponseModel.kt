package com.example.plazapalm.models

import com.google.gson.annotations.SerializedName

data class GetProfileResponseModel(
    val data: GetProfileData,
    val message: String,
    val status: Int
)

data class GetProfileData(
    @SerializedName("email") val email: String? = "",
    @SerializedName("first_name") val first_name: String? = "",
    @SerializedName("isFirstTimeSubscribedUser") val isFirstTimeSubscribedUser: Boolean? = false,
    @SerializedName("isPremium") val isPremium: Int? = 0,
    @SerializedName("last_name") val last_name: String? = "",
    @SerializedName("notification") val notification: Boolean? = false,
    @SerializedName("p_id") val p_id: String? = "",
    @SerializedName("profile_picture") val profile_picture: String?,
    @SerializedName("user_id") val user_id: String? = "",
    @SerializedName("user_name") val user_name: String? = ""
)

