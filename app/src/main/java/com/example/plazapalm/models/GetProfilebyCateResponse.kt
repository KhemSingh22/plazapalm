package com.example.plazapalm.models

import com.example.plazapalm.recycleradapter.AbstractModel

data class GetProfilebyCateResponse(
    val `data`: List<Datam>,
    val message: String,
    val status: Int
): AbstractModel()

data class Datam(
    val _id: String,
    val address: String,
    val c_id: String,
    val category_name: String,
    val created_at: String,
    val description_1: String,
    val description_2: String,
    val description_3: String,
    val expiry_date: Any,
    val first_name: String,
    val frontpage_bottom_text: String,
    val frontpage_top_text: String,
    val isAddedToCalender: Boolean,
    val isFavourite: Boolean,
    val isPremium: Int,
    val is_bottom_selected: Boolean,
    val is_top_selected: Boolean,
    val last_name: String,
    val lat: Double,
    val likeCount: Int,
    val location_text: String,
    val long: Double,
    val postProfile_picture: List<String>,
    val profile_title: String,
    val tags: String,
    val u_id: String,
    val user_name: String
)  : AbstractModel()