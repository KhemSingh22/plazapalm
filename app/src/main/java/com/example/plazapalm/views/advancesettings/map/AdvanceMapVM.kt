package com.example.plazapalm.views.advancesettings.map

import android.content.Context
import android.content.Intent
import android.location.GpsStatus
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.CommonMethods.context
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AdvanceMapVM @Inject constructor(

) : ViewModel() {
    private var locationManager: LocationManager? = null
    var gpsStatus: Boolean = false
    var isDarkModeClicked:ObservableBoolean= ObservableBoolean(false)

    var isClicked = ObservableBoolean(false)


    init {
        //CheckGpsStatus()
    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivAdvanceMapBackBtn -> {
                view.findNavController().navigateUp()
            }

          /*  R.layout.advance_map_bottom_sheet.apply {R.id.switchAdvanceMap}->
            {
                isDarkModeClicked.set(true)

            }*/
        }
    }

    fun CheckGpsStatus() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        assert(locationManager != null)

        val GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (GpsStatus === true) {
            CommonMethods.showToast(context, "GPS Is Enabled")
        } else {
            CommonMethods.showToast(context, "GPS Is Disabled")
        }
    }
}