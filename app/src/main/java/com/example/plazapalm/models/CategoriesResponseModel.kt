package com.example.plazapalm.models

import com.example.plazapalm.recycleradapter.AbstractModel

data class CategoriesResponseModel(
    val data: List<CategoriesData>?,
    val message: String?,
    val status: Int?
):AbstractModel()
data class CategoriesData(
    val _id: String? = "",
    val category_name: String = "",
    val count: String = "",
    var isCheck:Boolean?=false
) : AbstractModel()
