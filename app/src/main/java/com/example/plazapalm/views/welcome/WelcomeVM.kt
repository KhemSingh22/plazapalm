package com.example.plazapalm.views.welcome

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.plazapalm.R
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeVM @Inject constructor():ViewModel() {

    var signupType=ObservableField("")
    fun onClicks(view: View){
        when(view.id){
            R.id.ivWelcomeNextBtn->{
                val bundle=Bundle()
                bundle.putString("comingFrom",signupType.get())
                view.navigateWithId(R.id.categoriesListFragment,bundle)
            }
        }


    }
}