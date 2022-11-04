package com.example.plazapalm.views.advancesettings.editfontpage

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.CommonMethods.context
import com.example.plazapalm.utils.navigateWithId
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EditFrontPageVM @Inject constructor() : ViewModel() {

    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivAdvanceEditFrontPage -> {
                view.findNavController().navigateUp()
            }
            R.id.btnEditFrontPageView -> {
                view.navigateWithId(R.id.favDetailsFragment)
            }

            R.id.tvAdvanceEditFrontPageFontValue -> {
                //Open bottom sheet ..
                showBottomSheetDialogOne()
            }

        }

    }


    private fun showBottomSheetDialogOne() {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.fonts_list_fragment)
        val buttonCancel = dialog.findViewById<AppCompatTextView>(R.id.tvChooseFontCancel)
        buttonCancel?.setOnClickListener {
            CommonMethods.showToast(context, "clicked on Cancel")
        }
        dialog.show()
    }


}