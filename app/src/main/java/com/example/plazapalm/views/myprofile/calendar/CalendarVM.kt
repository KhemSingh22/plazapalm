package com.example.plazapalm.views.myprofile.calendar

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.models.CalendarModel
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarVM @Inject constructor(
) : ViewModel() {
    var calendarBookingList = ArrayList<CalendarModel>()
    val adapterCalendar by lazy { RecyclerAdapter<CalendarModel>(R.layout.calendar_list_item) }

    init {
        calendarBookingList.add(CalendarModel("Natalie Portman", "20 Dec 2021", "05:30 PM", "Los Angeles , CA"))
        calendarBookingList.add(
            CalendarModel(
                "Crish Evans ",
                "20 Dec 2021",
                "05:30 PM",
                "Los Angeles , CA"
            )
        )
        calendarBookingList.add(
            CalendarModel(
                "Daniel Redclief",
                "20 Dec 2021",
                "05:30 PM",
                "Los Angeles , CA"
            )
        )
        adapterCalendar.addItems(calendarBookingList)
        adapterCalendar.notifyDataSetChanged()

        adapterCalendar.setOnItemClick { view, _, type ->
            when (type) {
                "calendarBookingToChat" -> {
                    view.navigateWithId(R.id.action_calendarFragment_to_chatFragment)
                }
                "calendarBookingDetails" -> {
                    //navigate to Booking Details Fragment...
                    view.navigateWithId(R.id.action_calendarFragment_to_bookingDetailsFragment)
                }
            }
        }
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivCalendarBackBtn -> {
                view.findNavController().navigateUp()
            }
        }
    }
}