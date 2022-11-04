package com.example.plazapalm.premiumviews.qrcodegenerate

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QRCodeGenerateVM @Inject constructor() : ViewModel() {
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivQrCodeGenerateBack -> {
                view.findNavController().navigateUp()
            }
        }
    }
}