package com.example.plazapalm.models

import com.example.plazapalm.recycleradapter.AbstractModel

data class SearchCategoryData(
    var categoryName: String? = "",
    var isChecked:Boolean?=false,
    var categoryCounts: Int = 0
) : AbstractModel()