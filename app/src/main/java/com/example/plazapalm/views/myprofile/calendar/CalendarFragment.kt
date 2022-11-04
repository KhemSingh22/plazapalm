package com.example.plazapalm.views.myprofile.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.CalendarFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.calendar_fragment) {
    private var binding: CalendarFragmentBinding? = null
    private val viewModel: CalendarVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalendarFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //calendarClick()
        calendarClick()
        binding?.vm = viewModel
    }

    @SuppressLint("ResourceType")
    private fun calendarClick() {
        binding?.clCalendar?.setOnDateChangeListener { _, year, month, dayofMonth ->
            // In this Listener we are getting values
            // such as year, month and day of month
            // on below line we are creating a variable
            // in which we are adding all the variables in it.
            val date = (dayofMonth.toString() + "-" + (month + 1) + "-" + year)
            binding?.tvCalendar?.text = date
            // binding?.clCalendar?.dateTextAppearance = Color.RED


        }
    }

    /*  @SuppressLint("ResourceType")
      private fun calendar(){
          binding?.clCalendar?.setHeaderColor(resources.getColor(R.color.app_bar_light))
          //binding.clCalendar.setPages="[color]"
      }*/
}