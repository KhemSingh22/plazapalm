package com.example.plazapalm.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginDataModel(
    val data: LoginData?,
    val message: String?,
    val status: Int?
) : Serializable

data class LoginData(
    @SerializedName("email") val email: String? = "",
    @SerializedName("first_name") val first_name: String? = "",
    @SerializedName("follow") val follow: Boolean? = false,
    @SerializedName("isFirstTimeSubscribedUser") val isFirstTimeSubscribedUser: Boolean? = false,
    @SerializedName("isPremium") val isPremium: Int? = 0,
    @SerializedName("last_name") val last_name: String? = "",
    @SerializedName("location_OnOff") val location_OnOff: Boolean? = false,
    @SerializedName("notification") val notification: Boolean? = false,
    @SerializedName("token") val token: String? = "",
    @SerializedName("user_id") val user_id: String? = "",
    @SerializedName("user_name") val user_name: String? = "",
    @SerializedName("profileTitle") val profileTitle: String? = "",
    @SerializedName("address") val address: String? = ""

) : Serializable