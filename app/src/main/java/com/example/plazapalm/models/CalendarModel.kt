package com.example.plazapalm.models

import com.example.plazapalm.recycleradapter.AbstractModel

data class CalendarModel(
    var bookingUserName: String? = "",
    var bookingDate: String? = "",
    var bookingTime: String? = "",
    var bookingUserLocation: String? = ""
) : AbstractModel()