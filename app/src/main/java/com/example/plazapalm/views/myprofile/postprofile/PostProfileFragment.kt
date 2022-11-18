package com.example.plazapalm.views.myprofile.postprofile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
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
import com.example.plazapalm.views.myprofile.postprofile.adapter.ViewProAddImageAdapter
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
    lateinit var ImageList: ArrayList<AddPhoto>
    lateinit var viewProAddImageAdapter: ViewProAddImageAdapter
    var token = ObservableField("")
    var profileStatus = ObservableBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImageList = ArrayList()
        getBundledata()
        //  setAdapter()
        showRecyclerviewClick()
        token.set(pref.retrieveKey("token"))

    }

    private fun getBundledata() {
        if (arguments?.getString("comingFromView") != null) {
            Log.e("SSSSVVV",arguments?.get("userDATA").toString())

            if (arguments?.getString("comingFromView").equals("ViewPrfoile")) {
                viewModel.postdata.set("Update")

//                val userData:ObservableParcelable<postData?>  = arguments?.getParcelable<postData?>("userDATA") as ObservableParcelable<postData?>

                viewModel.firstName.set(arguments?.getString("f_name").toString())
                 viewModel.lastName.set(arguments?.getString("l_name").toString())
                 viewModel.userName.set(arguments?.getString("user_name").toString())
                 viewModel.profileTitle.set(arguments?.getString("pro_title").toString())
                 viewModel.p_id.set(arguments?.getString("p_id").toString())
                 viewModel.c_id.set(arguments?.getString("c_id").toString())
                 viewModel._id.set(arguments?.getString("_id").toString())

                val input = arguments?.getString("ex_date").toString()

                val split = input.split("T")

                val ex_date = split[0] //

                 viewModel.expireDate.set(ex_date.toString())
                 viewModel.address.set(arguments?.getString("addresss").toString())
                 viewModel.location.set(arguments?.getString("location_text").toString())
                 viewModel.long.set(arguments?.getString("longi").toString())
                 viewModel.lat.set(arguments?.getString("lati").toString())
                 viewModel.description1.set(arguments?.getString("des_1").toString())
                 viewModel.description2.set(arguments?.getString("des_2").toString())
                 viewModel.description3.set(arguments?.getString("des_3").toString())
                 viewModel.categeory.set(arguments?.getString("cate").toString())

                //IMGAE LIST
                 viewModel.photoList = arguments?.getParcelableArrayList<AddPhoto>("profile_Image") as ArrayList<AddPhoto>

                ImageList = arguments?.getParcelableArrayList<AddPhoto>("profile_Image") as ArrayList<AddPhoto>
              //  profileStatus.set(true)
                Log.e("SSSSVVV",arguments?.getString("des_3").toString() +"DFFDFDFD     " +
                        " "+ arguments?.getString("pro_title").toString() + "ccczxzxzxzx"
                        + arguments?.getStringArrayList("profile_Image"))

                Log.e("OSSKKKKSS",arguments?.getString("longi").toString() + "VVVCCC" + "vvv-----vvvv" +arguments?.getString("lati").toString())


            } else if (arguments?.getString(Constants.FROM_MY_PROFILE).equals("PostProfile")) {
                viewModel.postdata.set("Post")
            }
        }

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

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")
            ?.observe(viewLifecycleOwner) { data ->

                val datafromA = data
                // Split will return an array
                val split = datafromA.split("/")

                val categeroyName = split[0] // First element
                val c_id = split[1] // Second element

                viewModel.categeory.set(categeroyName)
                viewModel.c_id.set(c_id)
                Log.e("WWWWWWWW", data.toString() + "CIIDDDD" + c_id)
            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("bundle")
            ?.observe(viewLifecycleOwner) { data ->


                val datafromLocation = data
                // Split will return an array
                val split = datafromLocation.split("/")

                val longi = split[0] // First element
                val lati = split[1] // Second element
                val address = split[2] // Second element

                viewModel.location.set(address)
                viewModel.long.set(longi)
                viewModel.lat.set(lati)

                Log.e("WWWWWWWW", data.toString())


            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("photos")
            ?.observe(viewLifecycleOwner) { data ->

                Log.e("WERWEFDSFD", data.toString())

                val myType = object : TypeToken<ArrayList<AddPhoto>>() {}.type
                val photoList = Gson().fromJson<ArrayList<AddPhoto>>(data, myType)

                profileStatus.set(false)

                viewModel.photoList = photoList
                ImageList = photoList as ArrayList<AddPhoto>/* = java.util.ArrayList<kotlin.String> */
                Log.e("WERWEFDSFDczcxczxczxc", photoList.toString())

                setAdapter()
            }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {

        binding?.rvViewEditAddImages?.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewProAddImageAdapter = ViewProAddImageAdapter(this@PostProfileFragment, ImageList,profileStatus)
        binding?.rvViewEditAddImages?.adapter = viewProAddImageAdapter

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

        Log.e("SSSS", "WWW")

    }

}