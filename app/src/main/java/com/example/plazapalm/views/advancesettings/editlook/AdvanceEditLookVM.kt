package com.example.plazapalm.views.advancesettings.editlook

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdvanceEditLookVM @Inject constructor() : ViewModel() {
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivAdvanceEditLookBack -> {
                view.findNavController().navigateUp()
            }
            R.id.btnAdvanceEditLookView -> {
                view.findNavController().navigate(R.id.favDetailsFragment)
            }

        }
    }
}