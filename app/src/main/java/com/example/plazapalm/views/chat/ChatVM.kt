package com.example.plazapalm.views.chat

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.views.chat.adapter.ChatAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatVM @Inject constructor() : ViewModel() {
    var dialog: Dialog? = null
    var isClicked: ObservableBoolean = ObservableBoolean(false)
    val chatAdapter by lazy { ChatAdapter() }
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivChat -> {
                view.findNavController().navigateUp()
            }
            R.id.ivChatOpenBlock -> {
                if (isClicked.get()) {
                    isClicked.set(false)
                } else {
                    isClicked.set(true)
                }
            }
            R.id.tvBlockUserBtn -> {
                isClicked.set(false)
                showChooseOptionAccountDialog()
            }
        }
    }

    private fun showChooseOptionAccountDialog() {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.block_user_profile)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            /**choose options click(Button) **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvBlockProfile)?.setOnClickListener {
                dialog?.dismiss()
            }
            /**Edit Profile Click (Button)**/
            dialog?.findViewById<AppCompatTextView>(R.id.tvBlockCancelBtn)?.setOnClickListener {

                dialog?.dismiss()
            }

            dialog?.show()
        }
    }
}