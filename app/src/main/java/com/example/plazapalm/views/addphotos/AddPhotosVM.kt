package com.example.plazapalm.views.addphotos

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.BuildConfig
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.interfaces.ItemClickListener
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods.context
import com.example.plazapalm.views.addphotos.adapter.AddPhotosAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPhotosVM @Inject constructor(
    var dataStoreUtil: DataStoreUtil,
    var pref:PreferenceFile,
    var repository: Repository
    ) : ViewModel(), ItemClickListener {

    var dialog: Dialog? = null
    //    val addPhotosAdapter by lazy { AddPhotosAdapter(this) }
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivAddPhotosBackBtn -> {
                view.findNavController().navigateUp()
            }
            R.id.btnAddPhotos -> {
                view.findNavController().navigateUp()

            }
        }
    }


    override fun onClick(view: View, type: String, position: Int) {
        when (type) {
            context.getString(R.string.add_photo_type) -> {
                showChooseOptionAccountDialog()
            }
        }
    }

    private fun showChooseOptionAccountDialog() {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.open_camera_and_gallery)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            /** Take Photo Button  **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvTakePhotoBtn)?.setOnClickListener {
                //open camera

                dialog?.dismiss()
            }
            /**Take Gallery Button **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvTakePhotoGalleryBtn)
                ?.setOnClickListener {
                    dialog?.dismiss()
                }
            /** Take cancel Button **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvCancelBtn)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.show()
        }


    }


    private fun openGallery() {

    }

}