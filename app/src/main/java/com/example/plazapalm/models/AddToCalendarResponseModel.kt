package com.example.plazapalm.models

import java.io.Serializable

data class AddToCalendarResponseModel(
    val data: AddCalendarData?,
    val message: String?,
    val status: Int?
) : Serializable

data class AddCalendarData(
    val booking_id: String? = "",
    val booking_status: String? = "",
    val category_name: String? = "",
    val choose_date: String? = "",
    val choose_time: String? = "",
    val customer_id: String? = "",
    val description: String? = "",
    val post_profile_id: String? = ""
) : Serializable