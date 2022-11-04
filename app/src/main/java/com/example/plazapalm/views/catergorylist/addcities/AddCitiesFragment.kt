package com.example.plazapalm.views.catergorylist.addcities

import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.databinding.AddCitiesFragmentBinding
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddCitiesFragment : Fragment(R.layout.add_cities_fragment), OnMapReadyCallback {
    var addressLocation: String? = null

    @Inject
    lateinit var pref: PreferenceFile

    /**Initialized FusedLocation Client provider **/
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    /**Initialized Google Map **/
    lateinit var mMap: GoogleMap

    /**Current location is set to India, this will be of no use**/
    lateinit var currentLocation: LatLng
    lateinit var mapFragment: SupportMapFragment
    private var originLatng: LatLng? = null
    var currentaddress =""
    var Currentlati : Double?=null
    var Currentlongi  : Double?=null
    private var destinationLatng: LatLng? = null
    private var binding: AddCitiesFragmentBinding? = null
    private val viewModel: AddCitiesVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = AddCitiesFragmentBinding.inflate(layoutInflater)
        mapFragment = SupportMapFragment()
        val apiKey = getString(R.string.google_places_api)

        if (!Places.isInitialized()) {
            Places.initialize(requireActivity(), apiKey)
        }

        fetchApiKey()
        searchLocation()
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel

    }

    private fun fetchApiKey() {
        /**Fetching API_KEY which we wrapped**/
        val ai: ApplicationInfo =
            requireContext().applicationContext?.packageManager!!.getApplicationInfo(
                requireContext().applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        val value = ai.metaData[getString(R.string.google_map_api_key)]
        val apiKey = value.toString()
        /** Initialized Places Api key with the help of API -KEY **/
        if (!Places.isInitialized()) {
            Places.initialize(context?.applicationContext!!, apiKey)
        }
        /** Initializing Map..**/
        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment1) as SupportMapFragment
        mapFragment.getMapAsync(this)
        /** Initializing fused location client **/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        /**Adding Functionality on Map -location button **/
        binding?.btCurrentLocation?.setOnClickListener {
            getLastLocation()
        }
    }

    // Services such as getLastLocation()
    // will only run once map is ready
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.clear()
        getLastLocation()
        Log.e("SSSAAAA", "NOT___WORKINGGG")
    }

    // Get current location
    private fun getLastLocation() {
        if (CommonMethods.checkPermissions()) {
            if (CommonMethods.isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        CommonMethods.requestNewLocationData()
                    } else {
                        currentLocation = LatLng(location.latitude, location.longitude)

                        if (currentLocation!=null){

                            val geocoder: Geocoder
                            val addresses: List<Address>
                            geocoder = Geocoder(requireActivity(), Locale.getDefault())

                            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                            val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            val city = addresses[0].locality
                            val state = addresses[0].adminArea
                            val address = city + " " + state
                            currentaddress = address
                            Currentlati = location.latitude
                            Currentlongi = location.longitude

                            Log.e("ADASDASWQEWQE",address.toString() + "assad" + Currentlati + "XCXC"+ Currentlongi)
                            mMap.clear()
                            val markerOptions =
                                MarkerOptions().position(currentLocation).title(address.toString())
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                            //  .icon(BitmapDescriptorFactory.defaultMarker(HUE_AZURE))
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocation))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16F))
                            mMap.addMarker(markerOptions)

                        }

                    }
                }

            } else {
                CommonMethods.showToast(requireContext(), "Turn on Location")
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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

    private fun searchLocation() {
        binding!!.etSearchLocation.setOnClickListener {
            val field = listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME,
                Place.Field.ICON_URL
            )

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, field)
                .build(requireActivity())
            //start activity result
            startActivityForResult(intent, 101)

        }

        binding?.btAdd?.setOnClickListener {

           /* val bundle = Bundle()
            bundle.putString("comingFrom", "addCities")
            bundle.putDouble("longitude", originLatng?.longitude!!)
            bundle.putDouble("latitude", originLatng?.latitude!!)
            bundle.putString("addressFromCities", addressLocation)*/

            if (originLatng!=null){
                Currentlongi = originLatng?.longitude!!
                Currentlati = originLatng?.latitude!!
                currentaddress = addressLocation!!
            }

            pref.storeLocarion(currentaddress)

            if (arguments?.getString("PostProfile") != null) {

                findNavController().previousBackStackEntry?.savedStateHandle?.set("bundle", Currentlongi.toString() + "/" + Currentlati.toString() + "/" + currentaddress )
                findNavController().popBackStack()

                Log.e("SDSDSD", "DONEE")
            } else {
//                view?.navigateWithId(R.id.categoriesListFragment, bundle)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("bundle", Currentlongi.toString() + "/" + Currentlati.toString() + "/" + currentaddress)
                findNavController().popBackStack()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                //When success initialize place
                val place = Autocomplete.getPlaceFromIntent(data!!)
                addressLocation = place.address
                //set address on edittext
                viewModel.address.set(place.address)
                if (place.latLng != null) {
                    originLatng = place.latLng

                    if (originLatng != null) {
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(originLatng!!))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLatng!!, 18F))
                        Log.e("SSSAAAA", "WORKINGGG")
                    }

                    Log.e("TAGMMMMMMMMMV", place.latLng.toString())

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    val status: Status = Autocomplete.getStatusFromIntent(data)
                    //Log.i(TAG, status.getStatusMessage());
                } else if (resultCode == FragmentActivity.RESULT_CANCELED) {
                    // The user canceled the operation.
                }

            }
        }
    }


    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(false)
    }
}



