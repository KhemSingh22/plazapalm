package com.example.plazapalm.views.privacypolicy

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.hideKeyboard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyVM @Inject constructor() : ViewModel() {
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivPrivacyPolicyBtn->{
                view.findNavController().navigateUp()
            }
            R.id.clPrivacyPolicyMain->{
                CommonMethods.context.hideKeyboard()
            }
        }
    }
}