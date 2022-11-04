package com.example.plazapalm.views.favourites.favdetails

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FavDetailsFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.Constants
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavDetailsFragment : Fragment(R.layout.fav_details_fragment), OnMapReadyCallback,
    LocationSource.OnLocationChangedListener {
    private lateinit var marker: MarkerOptions
    lateinit var mMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment


    private var binding: FavDetailsFragmentBinding? = null
    private val viewModel: FavDetailsVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavDetailsFragmentBinding.inflate(layoutInflater)
        viewModel.isFavourites.set(true)
        CommonMethods.isAdvanceMap = true
        CommonMethods.statusBar(true)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchApiKey()
        setImagesAdapter()
        setVideosAdapter()
        binding?.vm = viewModel
        getFavoriteData()
        getbundleData()
    }

    private fun getbundleData() {
        val from = requireArguments().getString(Constants.FROM_MY_PROFILE)
        if (from.equals("ViewProfile")){
            binding!!.btnBookingProfile.visibility =View.GONE
        }
    }

    private fun fetchApiKey() {
        /**Fetching API_KEY which we wrapped**/
        val ai: ApplicationInfo =
            requireContext().applicationContext?.packageManager!!.getApplicationInfo(
                requireContext().applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        val apiKey = value.toString()
        /** Initialized Places Api key with the help of API -KEY **/
        if (!Places.isInitialized()) {
            Places.initialize(context?.applicationContext!!, apiKey)
        }
        /** Initializing Map..**/
        mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragmentViewProfile) as SupportMapFragment
        mapFragment.getMapAsync(this)
        /** Initializing fused location client **/
        CommonMethods.mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }


    /**This function used for Heart filled or Empty image if user coming from favorite screen then on next heart image will be filled else un-filled.**/
    private fun getFavoriteData() {
        if (arguments != null) {
            when (arguments?.get("comingFrom")) {
                "isFavorite" -> {
                    binding?.ivFavDetailsFilledHeart?.visibility = View.VISIBLE
                    binding?.ivFavDetailsEmptyHeart?.visibility = View.GONE

                }
                "isDashBoard" -> {
                    binding?.ivFavDetailsFilledHeart?.visibility = View.GONE
                    binding?.ivFavDetailsEmptyHeart?.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLastLocation()

        if (CommonMethods.isAdvanceMap === true) {
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_json_dark_mode
                )
            )
        } else {
            mMap.setMapStyle(null)
            CommonMethods.showToast(requireContext(), "Dark Mode is Disabled")
        }
    }

    // Get current location
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (CommonMethods.checkPermissions()) {
            if (CommonMethods.isLocationEnabled()) {
                CommonMethods.mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        CommonMethods.requestNewLocationData()
                    } else {
                        CommonMethods.currentLocation =
                            LatLng(location.latitude, location.longitude)
                        mMap.clear()
                        val markerOptions = MarkerOptions().position(CommonMethods.currentLocation)
                            .title("I am here! On Your Current Location")
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(CommonMethods.currentLocation))
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                CommonMethods.currentLocation,
                                15F
                            )
                        )
                        /* mMap.addMarker(MarkerOptions().position(latLng)
                                 .title("Your Destination is Here ")
                                 .snippet("Destination Description")
                         )*/
                        // addPolyGon()
                        mMap.isMyLocationEnabled = true
                        mMap.addMarker(markerOptions)


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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CommonMethods.advanceMap_Permission_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        mMap.clear()
    }


    private fun setImagesAdapter(){
        binding?.rvImages?.layoutManager=LinearLayoutManager(requireContext())
        binding?.rvImages?.adapter=viewModel.imagesAdapter
        binding?.rvImages?.adapter?.notifyDataSetChanged()
    }
    private fun setVideosAdapter(){
        binding?.rvVideos?.layoutManager=LinearLayoutManager(requireContext())
        binding?.rvVideos?.adapter=viewModel.videosAdapter
        binding?.rvVideos?.adapter?.notifyDataSetChanged()
    }
}