package com.example.plazapalm.models

data class SavePostProfileResponse(
    val `data`: Data,
    val message: String,
    val status: Int
)
data class Data(
    val __v: Int,
    val _id: String,
    val address: String,
    val c_id: String,
    val created_at: String,
    val dark_theme: Boolean,
    val description_1: String,
    val description_2: String,
    val description_3: String,
    val expiry_date: String,
    val first_name: String,
    val follow: Boolean,
    val font_size: Int,
    val frontpage_bottom_text: String,
    val frontpage_top_text: String,
    val is_bottom_selected: Boolean,
    val is_top_selected: Boolean,
    val last_name: String,
    val lat: Double,
    val location: Location,
    val location_OnOff: Boolean,
    val location_text: String,
    val long: Double,
    val postProfile_picture: List<String>,
    val profile_title: String,
    val status: Int,
    val tags: String,
    val u_id: String,
    val updated_at: String,
    val user_name: String
)

data class Location(
    val coordinates: List<Double>,
    val type: String
)