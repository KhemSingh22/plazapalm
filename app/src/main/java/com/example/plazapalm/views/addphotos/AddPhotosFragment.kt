package com.example.plazapalm.views.addphotos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.plazapalm.BuildConfig
import com.example.plazapalm.R
import com.example.plazapalm.databinding.AddPhotosFragmentBinding
import com.example.plazapalm.datastore.ADD_PHOTO_URI
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.interfaces.ItemClickListener
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.models.UploadMediaResponse
import com.example.plazapalm.networkcalls.ApiEnums
import com.example.plazapalm.networkcalls.ApiProcessor
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.networkcalls.RetrofitApi
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.views.addphotos.adapter.AddPhotosAdapter
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddPhotosFragment : Fragment(R.layout.add_photos_fragment), ItemClickListener {
    @Inject
    lateinit var dataStoreUtil: DataStoreUtil

    @Inject
    lateinit var pref: PreferenceFile

    @Inject
    lateinit var repository: Repository

    var binding: AddPhotosFragmentBinding? = null
    val viewModel: AddPhotosVM by viewModels()
    var photoFile: File? = null
    val GALARY_REQUEST_CODE = 201
    var photoList: ArrayList<AddPhoto> = ArrayList()
    lateinit var addPhotosAdapter: AddPhotosAdapter
    var dialog: Dialog? = null

    //  val addPhotosAdapter by lazy { AddPhotosAdapter(requireActivity(),this) }
    val REQUEST_CODEE = 200
    var pos: Int? = 0
    lateinit var bundle: Bundle
    val MY_CAMERA_PERMISSION_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddPhotosFragmentBinding.inflate(layoutInflater)

        photoList =
            requireArguments().getParcelableArrayList<AddPhoto>("imageList") as ArrayList<AddPhoto>


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
        //  photoList = ArrayList()
        bundle = Bundle()
        setAdapter()
        click()
    }

    private fun click() {
        binding!!.btnAddPhotos.setOnClickListener {


//            bundle?.putParcelableArrayList("Images",photoList)


//             var dataList=  photoList.filter { it.Image!="" } as ArrayList<AddPhoto>

            UploadMedia(requireView())

            /*  var arraylist=ArrayList<AddPhoto>()

              for(idx in 0 until photoList.size)
              {
                  if(photoList[idx].Image!="")
                  {
                      arraylist.add(photoList[idx])
                  }
              }

              Log.e("ASA",bundle!!.getString("khem").toString() + "VVVV")


              var gsonValue=Gson().toJson(arraylist)

              findNavController().previousBackStackEntry?.savedStateHandle?.set("photos",gsonValue)
              findNavController().popBackStack()

            //  bundle.putParcelableArrayList("photoList",dataList)
            //  requireActivity().supportFragmentManager.popBackStack()
  //            view!!.findNavController().navigate(R.id.action_addPhotosFragment_to_postProfileFragment,bundle)*/
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {

        binding?.rvAddPhotos?.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)


        var numOfPhotos = 6
        var pendingPhoto = 0
        if (photoList.size > 0) {
            pendingPhoto = numOfPhotos - photoList.size
        } else {
            pendingPhoto = 6
        }

        for (idx in 0 until pendingPhoto) {
            photoList.add(AddPhoto("", false))
        }

        addPhotosAdapter = AddPhotosAdapter(requireActivity(), photoList, this)
        binding?.rvAddPhotos?.adapter = addPhotosAdapter
        binding?.rvAddPhotos?.adapter?.notifyDataSetChanged()

    }

    var imagePath: String? = null

    @Throws(IOException::class)

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        imagePath = image.getAbsolutePath()
        return image
    }

    // var photoFile: File? = null
    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            //Create a file to store the image

            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }
            if (photoFile != null) {
                /* val photoURI: Uri =
                     FileProvider.getUriForFile(this, "com.inshorts.myapplication.android.provider", photoFile)*/
                val photoURI: Uri = FileProvider.getUriForFile(
                    Objects.requireNonNull(requireActivity()),
                    BuildConfig.APPLICATION_ID + ".fileprovider", photoFile!!
                )
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(pictureIntent, REQUEST_CODEE)
            }
        }
    }

    override fun onClick(view: View, type: String, position: Int) {
        pos = position
        when (type) {
            CommonMethods.context.getString(R.string.add_photo_type) -> {
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
            dialog?.setContentView(R.layout.open_camera_and_gallery)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            /** Take Photo Button  **/

            dialog?.findViewById<AppCompatTextView>(R.id.tvTakePhotoBtn)?.setOnClickListener {
                //open camera
                openCameraIntent()
                dialog?.dismiss()
            }

            /**Take Gallery Button **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvTakePhotoGalleryBtn)
                ?.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, GALARY_REQUEST_CODE)
                    dialog?.dismiss()
                }

            /** Take cancel Button **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvCancelBtn)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALARY_REQUEST_CODE && data != null) {

            val selectedImageURI: Uri = data.data!!
            var file = getPath(selectedImageURI)
            photoList.removeAt(pos!!)
            photoList.add(pos!!, AddPhoto(file.toString(), false))
            addPhotosAdapter.updateList(photoList, pos!!)
            //  bundle.putParcelableArrayList("photoList",photoList)
            //bundle.putString("DAMEO","DDDD")
            //   pref.storeImage("ADD_PHOTO_URI",photoList)
            addPhotosAdapter.notifyDataSetChanged()


        } else if (requestCode == REQUEST_CODEE &&
            resultCode == Activity.RESULT_OK
        ) {

            if (photoFile != null) {
                //    Glide.with(this).load(photoFile).into(ivPhoto!!)
//                    var photoFileData=Uri.fromFile(photoFile) as Uri
                var photoFileData = photoFile
                photoList.removeAt(pos!!)
                photoList.add(pos!!, AddPhoto(photoFileData.toString(), false))
                addPhotosAdapter.updateList(photoList, pos!!)
                addPhotosAdapter.notifyDataSetChanged()
                //  bundle.putParcelableArrayList("photoList",photoList)
                //   bundle.putString("DAMEO","xcxcxc")
                dataStoreUtil.savephoto(ADD_PHOTO_URI, photoList.toString())

            }
        }
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = requireActivity().managedQuery(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "camera permission granted", Toast.LENGTH_LONG)
                    .show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, REQUEST_CODE)
            } else {
                Toast.makeText(requireActivity(), "camera permission denied", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun UploadMedia(view: View) = lifecycleScope.launch {

        var surveyImagesParts: Array<MultipartBody.Part?>? = null

        var tempList =
            photoList!!.filter { it.isValid == false && it.Image != "" } as ArrayList<AddPhoto>


        if (tempList!!.size > 0) {
            Log.e("cXXZZZ", tempList.toString() + "VVVV")

            surveyImagesParts = arrayOfNulls<MultipartBody.Part>(tempList?.size!!)

            for (index in 0 until tempList!!.size) {

                val file = File(
                    tempList!!
                        .get(index)
//                      .toString()
                        .Image!!

                )

                Log.e("SDDDOOPPPP", file.toString())
                val surveyBody: RequestBody =
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)
                surveyImagesParts[index] =
                    MultipartBody.Part.createFormData("profile_picture", file.name, surveyBody)
                Log.e("SDDDSS-----SSSS", surveyBody.toString())

            }


            repository.makeCall(
                apiKey = ApiEnums.UPLOAD_IMAGES,
                loader = true,
                saveInCache = false,
                getFromCache = false,
                requestProcessor = object : ApiProcessor<Response<UploadMediaResponse>> {
                    override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<UploadMediaResponse> {
                        return retrofitApi.uploadMediaPostProfile(
                            Authorization = pref.retrieveKey("token"), surveyImagesParts!!
                        )
                    }

                    override fun onResponse(res: Response<UploadMediaResponse>) {
                        Log.e("HEADERR", res.body().toString())

                        var previousSelectedPhotos = photoList!!.filter { it.isValid == true } as ArrayList<AddPhoto>
                        var newList = ArrayList<String>()
                        newList.clear()
                        if (previousSelectedPhotos.size > 0) {
                            for (idx in 0 until previousSelectedPhotos.size) {
                                newList.add(previousSelectedPhotos[idx].Image.toString())
                            }
                        }

                        if (res.body()!!.data.size > 0) {
                            for (idx in 0 until res.body()!!.data.size) {
                                newList.add(res.body()!!.data[idx].toString())
                            }
                        }

                        var arraylist = ArrayList<AddPhoto>()

                        for (idx in 0 until newList.size) {
                            if (newList[idx].toString() != "") {
                                arraylist.add(AddPhoto(newList[idx],true))
                            }
                        }

                        Log.e("ASA", arraylist.toString() + "VVVV")

                        CommonMethods.showToast(requireActivity(), "images uploaded")
                        var gsonValue = Gson().toJson(arraylist)

                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            "photos",
                            gsonValue
                        )

                        findNavController().popBackStack()

                        //  bundle.putParcelableArrayList("photoList",dataList)
                        //  requireActivity().supportFragmentManager.popBackStack()
//            view!!.findNavController().navigate(R.id.action_addPhotosFragment_to_postProfileFragment,bundle)

                    }

                    override fun onError(message: String) {
                        super.onError(message)
                        CommonMethods.showToast(requireActivity(), message)

                    }
                })

        } /*else {

            val data_list = ArrayList<String>()
            for (idx in 0 until photoList!!.size){
                data_list.add(photoList!!.get(idx).Image.toString())
            }

            Log.e("DSFSFSFSFSFSF",data_list.toString())

            if (postdata.get().toString().equals("Post")) {
                SavePostProfileAPI(view, data_list)
            } else if (postdata.get().toString().equals("Update")) {
                editProfileAPI(view, data_list)
            }

        }*/
    }

}