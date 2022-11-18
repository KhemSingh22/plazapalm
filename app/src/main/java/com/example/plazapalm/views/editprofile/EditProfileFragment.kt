package com.example.plazapalm.views.editprofile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.plazapalm.R
import com.example.plazapalm.databinding.EditProfileFragmentBinding
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.datastore.PROFILE_DATA
import com.example.plazapalm.imageutils.FilePathUtil
import com.example.plazapalm.models.GetProfileResponseModel
import com.example.plazapalm.networkcalls.IMAGE_LOAD_URL
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.edit_profile_fragment) {
    @Inject
    lateinit var dataStoreUtil: DataStoreUtil
    private var binding: EditProfileFragmentBinding? = null
    private val viewModel: EditProfileVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        binding = EditProfileFragmentBinding.inflate(inflater)
         setProfileImage()
       // getMainActivityData()
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        binding?.vm = viewModel
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun onClicks() {
        binding?.ivEditProfileCamera?.setOnClickListener {
            Log.e("SD","SDDSSD")
            showCameraGalleryEditProfile()
        }
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
               requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), viewModel.REQUEST_PERMISSION.get()
            )
        }
    }
    /*Show camera and gallery alert dialog..*/
    private fun showCameraGalleryEditProfile() {
        CommonMethods.dialog
        if (CommonMethods.dialog != null && CommonMethods.dialog?.isShowing!!) {
            CommonMethods.dialog?.dismiss()
        } else {
            CommonMethods.dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            CommonMethods.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            CommonMethods.dialog?.setContentView(R.layout.camera_and_gallery_edit_profile)
            CommonMethods.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            CommonMethods.dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            CommonMethods.dialog?.setCancelable(false)
            /** Take Photo Button  **/
            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvEditProfileCamera)
                ?.setOnClickListener {
                    openCamera()
                    CommonMethods.dialog?.dismiss()
                }
            /**Take Gallery Button **/
            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvEditProfileGallery)
                ?.setOnClickListener {
                    openGallery()
                    CommonMethods.dialog?.dismiss()
                }

            CommonMethods.dialog?.findViewById<AppCompatTextView>(R.id.tvEditProfileCancel)
                ?.setOnClickListener {

                    CommonMethods.dialog?.dismiss()
                }
            CommonMethods.dialog?.show()
        }
    }

    /**Open Gallery Using Intent **/
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, viewModel.REQUEST_PICK_IMAGE.get())
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, viewModel.REQUEST_IMAGE_CAPTURE.get())
    }

    /** Now Getting Activity Result for camera and gallery  **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == viewModel.REQUEST_IMAGE_CAPTURE.get()) {
                if (data?.extras?.get("data")!=null){
                    val bitmap= data.extras?.get("data") as Bitmap
                    /*Save Image in File..*/
                    val file = FilePathUtil.saveImage(requireContext(), bitmap, compression = true)
                    Log.e("After Compression Size", file?.length().toString())
                    viewModel.imageFile = file
                    viewModel.uploadImage(file!!)
                }
            }
            else if (requestCode == viewModel.REQUEST_PICK_IMAGE.get()) {
                data?.data
                if (data?.data != null) {
                    val file = FilePathUtil.getMediaFilePathFor(requireContext(), data.data!!, compression = true)
                    Log.e("After Compression Size", file.length().toString())
                    viewModel.imageFile = file
                    viewModel.uploadImage(file)
                }
            }
        }
    }


    private fun setProfileImage() {
        dataStoreUtil.readObject(PROFILE_DATA, GetProfileResponseModel::class.java) {
            val uri = it?.data?.profile_picture
            //  val bitmap=MediaStore.Images.Media.getBitmap(context?.contentResolver,ur)
            if (uri != "") {
                Glide.with(this)
                    .load(IMAGE_LOAD_URL+uri).into(binding?.ivEditProfile!!)
            } else {
            }
        }
    }


}