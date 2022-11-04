package com.example.plazapalm.views.advancesettings.questionaries

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plazapalm.R
import com.example.plazapalm.databinding.QuestionariesFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.views.advancesettings.itemtouchhelper.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuestionariesFragment : Fragment(R.layout.questionaries_fragment) {
    private var binding: QuestionariesFragmentBinding? = null
    private val viewModel: QuestionariesVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QuestionariesFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // setQuestionAdapter()
        setQuestionsAdapter()
        binding?.vm = viewModel
    }

    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(true)
    }


    private fun setQuestionsAdapter(){
        binding?.rvQuestionary?.layoutManager=LinearLayoutManager(requireContext())
        binding?.rvQuestionary?.adapter=viewModel.questionsAdapter
        binding?.rvQuestionary?.adapter?.notifyDataSetChanged()

    }
    private fun setQuestionAdapter() {
        binding?.rvQuestionary?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuestionary?.adapter = SwipeToDeleteAdapter(viewModel.questionDataList)
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var adapter = binding?.rvQuestionary?.adapter as SwipeToDeleteAdapter
                showSwipeDeleteAccountDialog()
                adapter.removeAt(viewHolder.adapterPosition)
                adapter.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding?.rvQuestionary)
        binding?.rvQuestionary?.adapter?.notifyDataSetChanged()

    }

    fun showSwipeDeleteAccountDialog() {
        if (CommonMethods.dialog != null && CommonMethods.dialog?.isShowing!!) {
            CommonMethods.dialog?.dismiss()
        } else {
            CommonMethods.dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            CommonMethods.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            CommonMethods.dialog?.setContentView(R.layout.delete_swipe_alert)
            CommonMethods.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            CommonMethods.dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            CommonMethods.dialog?.setCancelable(false)
            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvSwipeDeleteBtn)
                ?.setOnClickListener {
                    CommonMethods.dialog?.dismiss()
                }
            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvDeleteSwipeCancelBtn)
                ?.setOnClickListener {
                    CommonMethods.dialog?.dismiss()
                }
        }
        CommonMethods.dialog?.show()
    }

}