package com.example.plazapalm.views.selectcategory.opencategeroy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.interfaces.clickItem
import com.example.plazapalm.models.CategoriesData
import com.example.plazapalm.models.CategoriesResponseModel
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.Constants
import com.example.plazapalm.utils.navigateWithId
import com.example.plazapalm.views.selectcategory.adapters.SelectCateAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@SuppressLint("NotifyDataSetChanged")
@HiltViewModel
class OpenCategeroyViewModel @Inject constructor(
    var dataStoreUtil: DataStoreUtil,
    var cacheUtil: CacheUtil,
    var pref: PreferenceFile,
    var repository: Repository
) : ViewModel() {
    var position: Int = -1
    var isChecked = ObservableBoolean(false)
    var token = ObservableField("")
    var page = ObservableField(1)
    var searchText = ObservableField("")
    var isClicked: Boolean = false
    var latitude = ObservableField("")
    var longitude = ObservableField("")
    var address = ObservableField("")
    var selectCateAdapter: SelectCateAdapter? = null

    init {
        /*get Token from Data store*/
        token.set(pref.retrieveKey("token"))

    }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.clSearchLocation -> {
                val bundle = Bundle()
                bundle.putString("CommingFrom", "OpenCategery")
                view.navigateWithId(R.id.action_openCategeroyFragment_to_addCitiesFragment)

            }
        }

    }


    fun getCategoriesApi(
        rvCategoryLocation: RecyclerView,
        requireActivity: FragmentActivity,
        clickItem: clickItem
    ) = viewModelScope.launch {
        val body = JSONObject()
        body.put(Constants.AUTHORIZATION, token.get())
        // body.put("lat", "30.8987")
        body.put("lat", latitude.get())
        body.put("long", longitude.get())
        // body.put("long", "76.7179")
        body.put("offset", page.get()!!)
        body.put("limit", 10)
        body.put("search=", searchText.get())

        Log.e("dsadas", latitude.get() + "  DFDF  " + longitude.get())

        repository.makeCall(
            apiKey = ApiEnums.GET_CATEGORIES,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<CategoriesResponseModel>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<CategoriesResponseModel> {
                    return retrofitApi.getCategories(
                        Authorization = token.get().toString(),
                        Lat = latitude.get()!!.toDouble(),
                        Long = longitude.get()!!.toDouble(),
                        OffSet = page.get()!!,
                        Limit = 100,
                        Search = searchText.get().toString()
                    )
                }

                override fun onResponse(res: Response<CategoriesResponseModel>) {

                    showCategories(
                        res.body()?.data!!,
                        rvCategoryLocation,
                        requireActivity,
                        clickItem
                    )
                }

            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showCategories(
        data: List<CategoriesData>,
        rvCategoryLocation: RecyclerView,
        requireActivity: FragmentActivity,
        clickItem: clickItem
    ) {
        rvCategoryLocation?.layoutManager = LinearLayoutManager(requireActivity)
        selectCateAdapter = SelectCateAdapter(requireActivity, data, clickItem, "OpenCategeroy")
        rvCategoryLocation?.adapter = selectCateAdapter
        rvCategoryLocation?.adapter?.notifyDataSetChanged()

    }

}