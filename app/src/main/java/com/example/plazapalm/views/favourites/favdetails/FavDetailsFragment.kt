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
import com.bumptech.glide.Glide
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FavDetailsFragmentBinding
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.models.AddImageDescriptionPOJO
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.models.FavData
import com.example.plazapalm.models.GetPostProfileResponse
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.recycleradapter.ViewPostProfileAdapter
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.Constants
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class FavDetailsFragment : Fragment(R.layout.fav_details_fragment), OnMapReadyCallback,
    LocationSource.OnLocationChangedListener {

    private lateinit var marker: MarkerOptions
    lateinit var mMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment

    @Inject
    lateinit var dataStoreUtil: DataStoreUtil

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var pref: PreferenceFile

    lateinit var dataList: ArrayList<String>

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
        dataList = ArrayList()
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchApiKey()
        //  setImagesAdapter()
//        setVideosAdapter()
        binding?.vm = viewModel
        getFavoriteData()
        Log.e("TOKKEN", pref.retrieveKey("token").toString())
    }

    private fun getbundleData() {
        val from = requireArguments().getString(Constants.FROM_MY_PROFILE)
        if (from.equals("ViewProfile")) {

            viewModel.isViewProfile.set(true)
            binding!!.btnBookingProfile.visibility = View.GONE

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
                    viewModel.CommingFrom.set("isFavorite")

                    binding?.ivFavDetailsFilledHeart?.visibility = View.VISIBLE
                    binding?.ivFavDetailsEmptyHeart?.setImageResource(R.drawable.ic_heart_filled_icon)
                    binding?.ivFavDetailsEmptyHeart?.visibility = View.GONE

                    /** GET DATA FROM FAVOURITES // BY SERIALIZABLE */
                    val data = requireArguments().getSerializable("ResBody") as ArrayList<FavData>
                    val pos = requireArguments().getInt("pos")
                    val image: ArrayList<String> =
                        data.get(pos).postProfile_picture as ArrayList<String>
                    setFavdata(pos, data, image)

                    Log.e(
                        "VVBBBB",
                        image.toString() + "  ---pos---  " + pos.toString() + data.toString() + "  ---pos---  "
                    )

                }
                "isDashBoard" -> {
                    viewModel.CommingFrom.set("isDashBoard")
                    binding?.ivFavDetailsFilledHeart?.visibility = View.GONE
                    binding?.ivFavDetailsEmptyHeart?.visibility = View.VISIBLE
                }
                "isViewProfile" -> {
//                    viewModel.isViewProfile.set(true)
                    viewModel.CommingFrom.set("isViewProfile")
                    binding!!.btnBookingProfile.visibility = View.GONE
                    getViewProfileData()
                }

            }

        }
    }

    private fun setFavdata(pos: Int, data: ArrayList<FavData>, image: ArrayList<String>) {

//        viewModel.isFav.set(data.get(pos).isfa)
//        viewModel.isLike.set(data.get(pos).isLiked!!)
//        viewModel.isDisLike.set(data.get(pos).isDisliked!!)
//        viewModel.LikesCount.set(data.get(pos).likeCount.toString())
//        viewModel.DisLikesCount.set(data.get(pos).dislikeCount.toString())

        if (viewModel.isFav.get()) {
            Glide.with(CommonMethods.context)
                .load(R.drawable.ic_heart_filled_icon)
                .into(binding!!.ivFavDetailsFilledHeart)
            viewModel.isFav.set(false)
        } else {
            Glide.with(CommonMethods.context)
                .load(R.drawable.ic_heart_icon)
                .into(binding!!.ivFavDetailsFilledHeart)
            viewModel.isFav.set(true)
        }

        dataList = image

        val dscList = ArrayList<String>()
        dscList.add(data.get(pos).description_1.toString())
        dscList.add(data.get(pos).description_2.toString())
        dscList.add(data.get(pos).description_3.toString())

        /// set data

//        viewModel.etVEditProDescription.set(data.get(pos).description_1.toString())
        viewModel.tvFavDetailsAddress.set(data.get(pos).address)
        viewModel.tvFavCityAddress.set(data.get(pos).location_text)
        viewModel.fav_title.set(data.get(pos).user_name)
        viewModel.username.set(data.get(pos).user_name)
        viewModel.p_id.set(data.get(pos).p_id)

        Glide.with(requireActivity())
            .load(
                IMAGE_LOAD_URL + image.get(
                    0
                )
            )
            .into(binding!!.ivFavDetails)

        Log.e("ASDASDASdasd", dataList.size.toString())

        setAdapter(dataList, dscList)

        binding!!.ivFavDetails

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

    private fun setVideosAdapter() {
        binding?.rvVideos?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvVideos?.adapter = viewModel.videosAdapter
        binding?.rvVideos?.adapter?.notifyDataSetChanged()
    }

    fun getViewProfileData() {

        if (pref.retrieveResponse() != null) {
            val data = pref.retrieveResponse()?.data
            val p_id = data?._id
            val lati = data?.lat
            val longi = data?.long
            viewModel.p_id.set(p_id)

            if (p_id != null) {
                if (lati != null) {
                    if (longi != null) {
                        getPostprofile(p_id, lati, longi)
                    }
                }
            }

            Log.e("RESSS", pref.retrieveResponse().toString())

        } else {
            Log.e("RESSS", "\"6273f2474ed0b9cd474553c7\", 30.7046, 76.7179")

            getPostprofile("6273f2474ed0b9cd474553c7", 30.7046, 76.7179)
        }

    }

    private fun getPostprofile(p_id: String, lati: Double, longi: Double) {

        Log.e("KKKKAAALLLL", p_id + " PID " + lati + " LAT  " + longi + " LONG ")

        repository.makeCall(
            ApiEnums.GET_POST_PROFILE,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<GetPostProfileResponse>> {

                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<GetPostProfileResponse> {
                    return retrofitApi.getPostProfile(
                        pref.retrieveKey("token").toString(),
                        p_id,
                        lati,
                        longi
                    )
                }

                override fun onResponse(res: Response<GetPostProfileResponse>) {
                    Log.e("AQQAAA", res.body().toString())

                    if (res.isSuccessful) {
                        if (res.body() != null) {
                            if (res.code() == 200) {
                                Log.e("QWQAAAZZZ", res.body().toString())

                                viewModel.userdata.set(res.body()!!.data)
                                viewModel.isFav.set(res.body()!!.data.isFavourite!!)
                                viewModel.isLike.set(res.body()!!.data.isLiked!!)
                                viewModel.isDisLike.set(res.body()!!.data.isDisliked!!)
                                viewModel.LikesCount.set(res.body()!!.data.likeCount.toString())
                                viewModel.DisLikesCount.set(res.body()!!.data.dislikeCount.toString())

                                if (viewModel.isFav.get()) {
                                    Glide.with(CommonMethods.context)
                                        .load(R.drawable.ic_heart_filled_icon)
                                        .into(binding!!.ivFavDetailsFilledHeart)
                                    viewModel.isFav.set(false)
                                } else {
                                    Glide.with(CommonMethods.context)
                                        .load(R.drawable.ic_heart_icon)
                                        .into(binding!!.ivFavDetailsFilledHeart)
                                    viewModel.isFav.set(true)
                                }

                                dataList =
                                    res.body()!!.data.postProfile_picture as ArrayList<String> /* = java.util.ArrayList<kotlin.String> */

                                var newDataList = ArrayList<AddPhoto>()
                                newDataList.clear()

                                for (idx in 0 until dataList.size) {
                                    newDataList.add(AddPhoto(dataList[idx].toString(), true))
                                }

                                viewModel.data_list = newDataList

                                val dsc1 = res.body()!!.data.description_1
                                val dsc2 = res.body()!!.data.description_2
                                val dsc3 = res.body()!!.data.description_3

                                val dscList = ArrayList<String>()
                                dscList.add(dsc1.toString())
                                dscList.add(dsc2.toString())
                                dscList.add(dsc3.toString())

                                /// set data

                                binding!!.etVEditProDescription.text = dsc1
                                binding!!.tvFavDetailsAddress.text = res.body()!!.data.address
                                binding!!.tvFavCityAddress.text = res.body()!!.data.location_text
                                binding!!.tvFavDetails.text = res.body()!!.data.user_name
                                viewModel.username.set(res.body()!!.data.user_name)

                                Glide.with(requireActivity())
                                    .load(
                                        IMAGE_LOAD_URL + res.body()!!.data.postProfile_picture?.get(
                                            0
                                        )
                                    )
                                    .into(binding!!.ivFavDetails)

                                Log.e("ASDASDASdasd", dataList.size.toString())

                                setAdapter(dataList, dscList)

                                binding!!.ivFavDetails

                            } else {
                                CommonMethods.showToast(CommonMethods.context, res.body()!!.message)
                            }
                        } else {
                            CommonMethods.showToast(CommonMethods.context, res.body()!!.message)
                        }
                    } else {
                        CommonMethods.showToast(CommonMethods.context, res.message())
                    }
                }

                override fun onError(message: String) {
                    super.onError(message)
                    Log.e("zxczxczxc", message)

                }

            }

        )
    }

    private fun setAdapter(data: ArrayList<String>, dscList: ArrayList<String>) {

        var dataList = ArrayList<AddImageDescriptionPOJO>()

        for (item in 1 until data.size) {
            var nameDes = ""
            if (item == 1) {
                nameDes = dscList[0]
            } else
                if (item == 2) {
                    nameDes = dscList[1]
                } else
                    if (item == data.size - 1) {
                        nameDes = dscList[2]
                    } else {
                        nameDes = ""
                    }
            dataList.add(AddImageDescriptionPOJO(data[item].toString(), nameDes, ""))
        }

        binding?.rvImages?.layoutManager = LinearLayoutManager(requireContext())

        val addapter = ViewPostProfileAdapter(requireActivity(), dataList)
        binding?.rvImages?.adapter = addapter
        binding?.rvImages?.adapter?.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        getFavoriteData()
    }

    override fun onStart() {
        super.onStart()
        getFavoriteData()
    }
}
