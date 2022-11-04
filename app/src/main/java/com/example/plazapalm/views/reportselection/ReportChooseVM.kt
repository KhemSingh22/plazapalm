package com.example.plazapalm.views.reportselection

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.models.ReportDataModal
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("NotifyDataSetChanged")
@HiltViewModel
class ReportChooseVM @Inject constructor() : ViewModel() {

    var isChecked=ObservableBoolean(false)
     var reportModelList: ArrayList<ReportDataModal> = ArrayList()
    val reportChooseAdapter by lazy { RecyclerAdapter<ReportDataModal>(R.layout.fragment_report_choose_option_items) }
    init {
        reportModelList.add(ReportDataModal("False, misleading or harmful,\n" + "Information", true))
        reportModelList.add(ReportDataModal("Inappropriate Profile", false))
        reportModelList.add(ReportDataModal("Fake/Spam", false))
        reportModelList.add(ReportDataModal("Outdated", false))
        reportModelList.add(ReportDataModal("Harassment or Hate Speech", false))
        reportModelList.add(ReportDataModal("Wrong Category", false))
        reportChooseAdapter.addItems(reportModelList)
        reportChooseAdapter.notifyDataSetChanged()
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivReportChooseBackBtn -> {
                view.findNavController().navigateUp()
            }
            R.id. btnReportChecked -> {
                view.navigateWithId(R.id.action_reportChooseOptionFragment_to_reportFragment)
            }
        }

    }
}