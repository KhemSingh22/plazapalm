package com.example.plazapalm.models
import com.example.plazapalm.recycleradapter.AbstractModel
data class QuestionsModel(
    var questionNumber: String?="",
    var questionValue: String?=""
) : AbstractModel()