package com.example.plazapalm.views.advancesettings.questionaries

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.models.QuestionsModel
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionariesVM @Inject constructor() : ViewModel() {
    var questionDataList = ArrayList<QuestionsModel>()
    var position = -1
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivQuestionaryBack -> {
                view.findNavController().navigateUp()
            }
            R.id.btnAddNewQuestions -> {
                /***open alert dialog for add questions...***/
                CommonMethods.openAddQuestionDialog()
            }
        }
    }

    val questionsAdapter by lazy { RecyclerAdapter<QuestionsModel>(R.layout.add_question_items) }

    init {
        questionDataList.add(QuestionsModel(" Question 1", "Hello Android "))
        questionDataList.add(QuestionsModel("Question 2", "How to straighteen the Hairs"))
        questionDataList.add(QuestionsModel("Question 3", "How may i help you"))
        questionDataList.add(QuestionsModel("Question 4", "Everything is fine..."))
        questionDataList.add(QuestionsModel("Question 5", "How make a good smoothy application"))
        questionDataList.add(QuestionsModel("Question 6", "How to straighteen the Hairs"))
        questionsAdapter.addItems(questionDataList)
        questionsAdapter.getAllItems()
        questionsAdapter.notifyDataSetChanged()
        questionsAdapter.setOnItemClick { _, _, type ->
            when (type) {
                "onSwipeItemClick" -> {
                    /**Swipe to Delete .... **/
                    CommonMethods.showSwipeDeleteAccountDialog()
                }
                else -> {

                }
            }
        }
    }


}

