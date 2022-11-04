package com.example.plazapalm.models

import com.example.plazapalm.recycleradapter.AbstractModel

data class UserMessageModal(
    var image: Int = 0,
    var name: String? = "",
    var time: String? = "",
    var description: String? = ""
) : AbstractModel()