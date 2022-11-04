package com.example.plazapalm.views.confirmbookthankyou

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.hideKeyboard
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmBookingVM @Inject constructor() : ViewModel() {
    fun onClicks(view: View) {
        when (view.id) {
            R.id.clMainConfirmBooking -> {
                CommonMethods.context.hideKeyboard()
            }
            R.id.ivConfirmDetailsBackBtn -> {
                view.findNavController().navigateUp()
            }

            R.id.ivBookingDetailsChat -> {
                view.navigateWithId(R.id.action_confirmBookingFragment_to_chatFragment)
            }
            R.id.btnConfirmBook -> {
                view.navigateWithId(R.id.bookingDetailsFragment)
            }
        }
    }
}