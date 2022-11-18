package com.example.plazapalm.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.plazapalm.databinding.ProgressLayoutBinding

private var customDialog: AlertDialog? = null

fun Context.showProgress() {
    hideProgress()
    val customAlertBuilder = AlertDialog.Builder(this)
    val customAlertView = ProgressLayoutBinding.inflate(LayoutInflater.from(this), null, false)
    customAlertBuilder.setView(customAlertView.root)
    customAlertBuilder.setCancelable(false)
    customDialog = customAlertBuilder.create()
    customDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    customDialog!!.show()
}

fun hideProgress() {
    if (customDialog != null && customDialog?.isShowing!!) {
        customDialog?.dismiss()
    }

}