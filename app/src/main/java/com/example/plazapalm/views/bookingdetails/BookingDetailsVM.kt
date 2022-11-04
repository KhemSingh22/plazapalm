package com.example.plazapalm.views.bookingdetails

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingDetailsVM @Inject constructor() : ViewModel() {

    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivBookingDetailsBack -> {
                view.findNavController().navigateUp()
            }

            R.id.btnBookingDetailsCancel -> {
                //Here Cancel Api will hit and navigate back to details page...
                view.navigateWithId(R.id.thankYouFragment)
            }


            R.id.ivFavDetailsOptions -> {
                //Here Navigate to Chat Screen...

                view.navigateWithId(R.id.action_bookingDetailsFragment_to_chatFragment)
            }

            R.id.tvBookingDetailViewProfile -> {
                //Here Navigate View profile Screen....
                view.navigateWithId(R.id.action_bookingDetailsFragment_to_favDetailsFragment)
            }
        }
    }
}