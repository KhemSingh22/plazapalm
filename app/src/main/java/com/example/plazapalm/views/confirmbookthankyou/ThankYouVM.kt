package com.example.plazapalm.views.confirmbookthankyou

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.plazapalm.R
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThankYouVM @Inject constructor():ViewModel() {
    fun onClicks(view: View){
        when(view.id){
            R.id.ivThankYouConfirmBookNextBtn->{
                view.navigateWithId(R.id.dashBoardFragment)
            }
        }
    }
}