package com.example.plazapalm.views.catergorylist.addcities

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.hideKeyboard
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCitiesVM @Inject constructor(
    var pref: PreferenceFile,
) : ViewModel(){
var address=ObservableField("Search for location")
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivBackLocation -> {
                view.findNavController().navigateUp()
            }
            R.id.clMainSelectCity -> {
                CommonMethods.context.hideKeyboard()
            }
        }

    }




}