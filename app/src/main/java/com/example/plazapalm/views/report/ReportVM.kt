package com.example.plazapalm.views.report

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.networkcalls.CacheUtil
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.hideKeyboard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportVM @Inject constructor(
    private var dataStoreUtil: DataStoreUtil,
    private var cacheUtil: CacheUtil,
    private var repository: Repository
) : ViewModel() {
    fun clicks(view: View) {
        when (view.id) {
            R.id.ivReportBackButton -> {
                view.findNavController().navigateUp()
            }

            R.id.clReportMain -> {
                CommonMethods.context.hideKeyboard()
            }
        }
    }
}