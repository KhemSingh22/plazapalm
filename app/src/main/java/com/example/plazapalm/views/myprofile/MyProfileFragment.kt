package com.example.plazapalm.views.myprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.MyProifleFragmentBinding
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.datastore.LOGIN_DATA
import com.example.plazapalm.models.LoginDataModel
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.Constants
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : Fragment(R.layout.my_proifle_fragment) {
    @Inject
    lateinit var dataStoreUtil: DataStoreUtil
    @Inject
    lateinit var pref: PreferenceFile
    private var binding: MyProifleFragmentBinding? = null
    private val viewModel: MyProfileVM by viewModels()
    var postStatus = ObservableBoolean()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyProifleFragmentBinding.inflate(inflater, container, false)
        premiumAccount()
        CommonMethods.statusBar(false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        viewModel.getProfile()
        //  getDataFromLocal()
        viewModel.myProfileData()
        getstatus()
        binding?.vm = viewModel
    }

    private fun getstatus() {
        if (pref.retrieveBoolKey(Constants.POSTSTATUS)!!.equals(true)){
            Log.e("SDSDSDSDSDS",pref.retrieveBoolKey(Constants.POSTSTATUS).toString())
            viewModel.status.set("View Profile")
            viewModel.changestatus.set(true)
        }else{
            viewModel.status.set("Post a Profile")
            viewModel.changestatus.set(false)

        }
    }

    /*click functionality..*/
    private fun onClicks() {
        binding?.ivMyProfileScanner?.setOnClickListener {
            view?.navigateWithId(R.id.qrCodeScannerFragment)
        }
    }

    private fun premiumAccount() {
        if (viewModel.premium == 1) {
            binding?.tvAdvanceSetting?.visibility = View.VISIBLE
            binding?.viewAdvanceSettings?.visibility = View.VISIBLE
        } else {
            binding?.tvAdvanceSetting?.visibility = View.GONE
            binding?.viewAdvanceSettings?.visibility = View.GONE

            binding?.tvUpgrade?.visibility = View.VISIBLE
            binding?.viewUpgrade?.visibility = View.VISIBLE
        }
    }


    private fun getDataFromLocal() {
        dataStoreUtil.readObject(LOGIN_DATA, LoginDataModel::class.java) {
            // viewModel.storedImageUrl.set(it?.data?.profile_picture.toString())
            /* if (viewModel.storedImageUrl.get() != "") {
                 viewModel.storedImageUrl.set(it?.data?.profile_picture)
                 //  val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                 Glide.with(this)
                     .load(IMAGE_LOAD_URL + viewModel.storedImageUrl.get())
                     .into(binding?.ivMyProfile!!)
             } else {
                 binding?.ivMyProfile?.setImageResource(R.drawable.dash_items_nurse_image)
             }*/
            binding?.tvMyProfileName?.text = it?.data?.first_name + " " + it?.data?.last_name

        }
    }

}