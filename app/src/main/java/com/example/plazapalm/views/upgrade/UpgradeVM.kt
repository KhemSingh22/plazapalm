package com.example.plazapalm.views.upgrade

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.plazapalm.R
import com.example.plazapalm.utils.navigateBack
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpgradeVM @Inject constructor() : ViewModel() {

    fun onClicks(view: View) {
        when (view.id) {
              R.id.tvUpgradeNotNow->{
                  view.navigateBack()
              }
        }
    }
}