package com.example.plazapalm.imageutils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CameraBottomSheet(
    private val galleryResult: ActivityResultLauncher<Intent>,
    private val cameraResult: ActivityResultLauncher<Intent>,
    private val activity: Activity,
) : BottomSheetDialogFragment() {
    // private lateinit var cameraSheetBinding: ItemChooserBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        //  cameraSheetBinding = ItemChooserBinding.inflate(LayoutInflater.from(context))
        // dialog.setContentView(cameraSheetBinding.root)
        dialog.setCancelable(false)
        /* cameraSheetBinding.tvImage.setOnClickListener {
             takeImage(activity, cameraResult, dialog)
         }*/

        /* cameraSheetBinding.tvGallery.setOnClickListener {
             selectImage(activity, galleryResult, dialog)
         }*/


/*
        (cameraSheetBinding.root.parent as View).setBackgroundColor(Color.TRANSPARENT)
*/
    }


    private fun takeImage(
        activity: Activity, cameraResult: ActivityResultLauncher<Intent>,
        dialog: Dialog
    ) {
/*
        activity.storagePermission { permissionGranted ->

            if (permissionGranted) {
                try {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraResult.launch(takePicture)
                    dialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                activity.permissionAlert(cameraPermission = true) { canRequest ->
                    if (canRequest)
                        takeImage(activity, cameraResult, dialog)
                    else
                        dialog.dismiss()
                }
            }
        }
*/
    }

    private fun selectImage(
        activity: Activity,
        galleryResult: ActivityResultLauncher<Intent>,
        dialog: Dialog
    ) {
/*
        activity.storagePermission { permissionGranted ->
            if (permissionGranted) {
                try {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickPhoto.type = "image/*"
                    galleryResult.launch(pickPhoto)
                    dialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                activity.permissionAlert(storagePermission = true) { canRequest ->
                    if (canRequest)
                        selectImage(activity, galleryResult, dialog)
                    else
                        dialog.dismiss()
                }
            }
        }
*/
    }
}
 */


    }
}