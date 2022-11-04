package com.example.plazapalm.models

import java.io.Serializable

data class ContactUsResponseModel(
    val `data`: ContactUsData,
    val message: String,
    val status: Int
):Serializable

data class ContactUsData(
    val __v: Int? = 0,
    val _id: String? = "",
    val email: String? = "",
    val message: String? = "",
    val subject: String? = "",
    val u_id: String? = ""
):Serializable