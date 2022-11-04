package com.example.plazapalm.views.myprofile.postprofile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.PostProfileFragmentBinding
import com.example.plazapalm.interfaces.ItemClickListener
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.Constants
import com.example.plazapalm.views.addphotos.adapter.AddPhotosAdapter
import com.example.plazapalm.views.myprofile.postprofile.adapter.ViewProAddImageAdapter
import com.google.android.datatransport.cct.internal.LogResponse.fromJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PostProfileFragment : Fragment(R.layout.post_profile_fragment), ItemClickListener {
    @Inject
    lateinit var pref: PreferenceFile
    @Inject
    lateinit var repository: Repository
    private var binding: PostProfileFragmentBinding? = null
    private val viewModel: PostProfileVM by viewModels()
//    lateinit var ImageList: ArrayList<AddPhoto>
    lateinit var ImageList: ArrayList<String>
    lateinit var viewProAddImageAdapter : ViewProAddImageAdapter
    lateinit var addPhotosAdapter : AddPhotosAdapter
    var token = ObservableField("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImageList = ArrayList()

      //  setAdapter()
        showRecyclerviewClick()
        token.set(pref.retrieveKey("token"))

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PostProfileFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel


        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(viewLifecycleOwner) { data ->

                val datafromA = data
                // Split will return an array
                val split = datafromA.split("/")

                val categeroyName = split[0] // First element
                val c_id = split[1] // Second element

                viewModel.categeory.set(categeroyName)
                viewModel.c_id.set(c_id)
                Log.e("WWWWWWWW",data.toString() + "CIIDDDD" + c_id)

        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("bundle")?.observe(viewLifecycleOwner) { data ->


            val datafromLocation = data
            // Split will return an array
            val split = datafromLocation.split("/")

            val longi = split[0] // First element
            val lati = split[1] // Second element
            val address = split[2] // Second element

            viewModel.location.set(address)
            viewModel.long.set(longi)
            viewModel.lat.set(lati)

            Log.e("WWWWWWWW",data.toString())


        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("photos")?.observe(viewLifecycleOwner) { data ->

            val myType = object : TypeToken<ArrayList<String>>() {}.type
            val photoList = Gson().fromJson<ArrayList<String>>(data, myType)

            viewModel.photoList = photoList
            ImageList = photoList as ArrayList<String> /* = java.util.ArrayList<kotlin.String> */
            Log.e("WERWEFDSFD",data.toString())
            Log.e("WERWEFDSFDczcxczxczxc",photoList.toString())
            setAdapter()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {

//                ImageList = arguments?.getParcelableArrayList("photoList")!!
//                viewModel.photoList = ImageList
                binding?.rvViewEditAddImages?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                viewProAddImageAdapter = ViewProAddImageAdapter(this@PostProfileFragment,ImageList)
                binding?.rvViewEditAddImages?.adapter = viewProAddImageAdapter
                //binding?.rvViewEditAddImages?.adapter?.notifyDataSetChanged()



//        addPhotosAdapter = AddPhotosAdapter(requireActivity(),ImageList, this)
//        binding?.rvViewEditAddImages?.adapter = addPhotosAdapter
            /* val gson = Gson()
             if(pref.retrieveKey("ADD_PHOTO_URI")!=null){
                 val key  : String = pref.retrieveKey("ADD_PHOTO_URI")!!
                 var type : Type = object : TypeToken<ArrayList<AddPhoto?>?>() {}.type

                 ImageList = gson.fromJson(key, type)
                 Log.e("AAAAAAAA",ImageList.toString())
             }*/


//        binding?.rvViewEditAddImages?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding?.rvViewEditAddImages?.adapter = viewModel.addImagesAdapter
//        binding?.rvViewEditAddImages?.adapter?.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(true)
        if (viewModel.isClicked.get()) {
            viewModel.isClicked.set(true)
        } else {
            viewModel.isClicked.set(false)
        }
        setAdapter()
    }

    private fun showRecyclerviewClick() {

        if (viewModel.isClicked.get()) {
            viewModel.isClicked.set(true)
        } else {
            viewModel.isClicked.set(false)
        }
    }

    override fun onClick(view: View, type: String, position: Int) {

        Log.e("SSSS","WWW")

    }

}