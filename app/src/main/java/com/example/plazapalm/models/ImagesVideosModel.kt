package com.example.plazapalm.models

import com.example.plazapalm.recycleradapter.AbstractModel


data class ImagesVideosModel(
    var uploadImages: Int?,
    var uploadVideos: String?
) : AbstractModel()