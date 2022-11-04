package com.example.plazapalm.views.advancesettings.pictures

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
import com.example.plazapalm.models.PicturesModel
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PicturesVM @Inject constructor() : ViewModel() {
    var picturesDataList = ArrayList<PicturesModel>()
    val picturesAdapter by lazy { RecyclerAdapter<PicturesModel>(R.layout.pictures_item_list) }
    init {
        picturesDataList.add(PicturesModel(R.drawable.dash_items_nurse_image, 0))
        picturesDataList.add(PicturesModel(R.drawable.dash_items_nurse_image, 0))
        picturesDataList.add(PicturesModel(R.drawable.dash_items_nurse_image, 0))
        picturesDataList.add(PicturesModel(R.drawable.dash_items_nurse_image, 0))
        picturesDataList.add(PicturesModel(R.drawable.dash_items_nurse_image, 0))
        picturesDataList.add(PicturesModel(R.drawable.dash_items_nurse_image, 0))
        picturesDataList.add(PicturesModel(R.drawable.dash_items_nurse_image, 0))

        picturesAdapter.addItems(picturesDataList)
        picturesAdapter.notifyDataSetChanged()
        picturesAdapter.setOnItemClick { view, position, type ->
            when (type) {
                "deleteImageVideo" -> {
                    //Delete Api Call Here..
                }
                "picturesItemClick" -> {
                    //Navigate to show image or video on new  screen.
                      view.navigateWithId(R.id.action_picturesFragment_to_showPictureVideoFragment)
                }
            }
        }
    }
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivPictureAddBackBtn -> {
                view.findNavController().navigateUp()
            }
            R.id.ivPicturesAdd -> {
                //open dialog alert to upload image or videos.
                uploadImageVideo()
            }
        }
    }
    private fun uploadImageVideo() {
        if (CommonMethods.dialog != null && CommonMethods.dialog?.isShowing!!) {
            CommonMethods.dialog?.dismiss()
        } else {
            CommonMethods.dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            CommonMethods.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            CommonMethods.dialog?.setContentView(R.layout.pictures_item_upload_image_videos)
            CommonMethods.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            CommonMethods.dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            CommonMethods.dialog?.setCancelable(false)
            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvUploadImage)
                ?.setOnClickListener {
                    CommonMethods.dialog?.dismiss()
                }
            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvUploadVideo)
                ?.setOnClickListener {
                    CommonMethods.dialog?.dismiss()
                }
            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvPicturesUploadCancel)
                ?.setOnClickListener {
                    CommonMethods.dialog?.dismiss()
                }
        }
        CommonMethods.dialog?.show()
    }
}





