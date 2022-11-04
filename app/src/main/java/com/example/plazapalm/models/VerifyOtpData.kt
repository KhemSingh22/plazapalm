package com.example.plazapalm.models

import java.io.Serializable


class VerifyOtpData(
    val data: VerifyData?,
    val message: String?,
    val status: Int?
):Serializable
data class VerifyData(
    val email: String?="",
    val first_name: String?="",
    val last_name: String?="",
    val notification: Boolean?=false,
    val token: String?="",
    val user_id: String?=""
):Serializable