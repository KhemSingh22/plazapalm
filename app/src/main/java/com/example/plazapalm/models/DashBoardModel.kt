package com.example.plazapalm.models

import com.example.plazapalm.recycleradapter.AbstractModel

data class DashBoardModel(
    var name: String,
    var description: String,
    var locationName: String,
    var image: Int,
    var miles: Int,
) : AbstractModel()
