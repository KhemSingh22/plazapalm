package com.example.plazapalm.models

import java.io.Serializable

data class SignupResponseModel(
    val data: SignupResponseData,
    val message: String,
    val status: Int
) : Serializable

data class SignupResponseData(
    val email: String? = "",
    val userId: String? = ""
) : Serializable