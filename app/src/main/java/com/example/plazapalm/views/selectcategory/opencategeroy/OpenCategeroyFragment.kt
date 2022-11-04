package com.example.plazapalm.views.selectcategory.opencategeroy

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FragmentOpenCategeroyBinding
import com.example.plazapalm.interfaces.clickItem
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.navigateWithId
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OpenCategeroyFragment : Fragment(R.layout.fragment_open_categeroy), clickItem {

    private var binding: FragmentOpenCategeroyBinding? = null
    private val viewmodel: OpenCategeroyViewModel by viewModels()
    private lateinit var mFusedLocation: FusedLocationProviderClient

    @Inject
    lateinit var pref: PreferenceFile
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOpenCategeroyBinding.inflate(inflater, container, false)
        mFusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())

        getLastLocation()
        return binding?.root




    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.vm = viewmodel

        getdata()

        if (pref.retrieveLocarion() != null) {
            viewmodel.address.set(pref.retrieveLocarion())
            Log.e("QQQQQQ",pref.retrieveLocarion().toString())
        }
    }

    private fun getdata() {
        binding?.tvCategories?.visibility = View.VISIBLE
        viewmodel.isChecked.set(false)
        binding?.rvCategoryLocation?.isSelected = false
        viewmodel.getCategoriesApi(binding!!.rvCategoryLocation, requireActivity(), this)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("bundle")
            ?.observe(viewLifecycleOwner) { data ->

                val datafromLocation = data
                // Split will return an array
                val split = datafromLocation.split("/")

                val longi = split[0] // First element
                val lati = split[1] // Second element
                val address = split[2] // Second element

                viewmodel.address.set(address)
                viewmodel.longitude.set(longi)
                viewmodel.latitude.set(lati)
                viewmodel.address.set(address)

                Log.e("ADDKFJSDFJSDJF", data.toString() + "CIIDDDD" + address)

            }
    }

    override fun click(categoryName: String, position: Int, c_id: String?, s: String) {
        val bundle = Bundle()
        bundle.putString("fromOpencate", "openCategeroy")
        bundle.putString("cateName", categoryName)
        bundle.putString("c_id", c_id)
        bundle.putString("longitude", viewmodel.longitude.get())
        bundle.putString("latitude", viewmodel.latitude.get())
        bundle.putString("status", "OPEN")

        view?.navigateWithId(R.id.action_openCategeroyFragment_to_dashBoardFragment, bundle)

        Log.e("SFSDs", categoryName.toString())
    }

    // Get current location
    private fun getLastLocation() {
        if (CommonMethods.checkPermissions()) {
            if (CommonMethods.isLocationEnabled()) {
                mFusedLocation.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        CommonMethods.requestNewLocationData()
                    } else {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        viewmodel.latitude.set(list[0].latitude.toString())
                        viewmodel.longitude.set(list[0].longitude.toString())
                        list[0].countryName
//                        viewmodel.address.set(list[0].locality.toString())
                        Log.e("countryName", "" + list[0].locality + "" + list[0].countryName)
                    }
                }

            } else {
                CommonMethods.showToast(requireContext(), "Turn on Location")
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            CommonMethods.requestPermissions()
        }
    }

    // What must happen when permission is granted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CommonMethods.pERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(false)
    }

}