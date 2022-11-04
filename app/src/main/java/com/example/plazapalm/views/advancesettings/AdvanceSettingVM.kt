package com.example.plazapalm.views.advancesettings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdvanceSettingVM @Inject constructor(

) : ViewModel() {


    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivAdvanceSettingBack -> {
                // Navigate to Advance Setting screen
                view.findNavController().navigateUp()
            }
            R.id.tvAdvanceSettingEditLook -> {
                // Navigate to Advance Setting Edit Look Screen
                view.navigateWithId(R.id.action_advanceSettingFragment_to_advanceEditLookFragment)
            }
            R.id.tvAdvanceSettingEditFrontPage -> {
                // Navigate to Advance Setting Edit Front Page
                view.navigateWithId(R.id.action_advanceSettingFragment_to_editFrontPageFragment)
            }
            R.id.tvAdvanceSettingMap -> {
                //Navigate to Advance Setting Map  screen
                view.navigateWithId(R.id.action_advanceSettingFragment_to_advanceMapFragment)
            }
            R.id.tvAdvanceSettingGallery -> {
                // Navigate to Advance Setting Map  screen
                view.navigateWithId(R.id.action_advanceSettingFragment_to_picturesFragment)
            }
            R.id.tvAdvanceSettingQuestions -> {
                view.navigateWithId(R.id.action_advanceSettingFragment_to_questionariesFragment)
            }



            R.id.tvAdvanceSettingQuestions -> {
                view.navigateWithId(R.id.action_advanceSettingFragment_to_questionariesFragment)
            }
        }
    }
    
    
}