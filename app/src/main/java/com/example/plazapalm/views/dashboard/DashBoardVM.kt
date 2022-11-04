package com.example.plazapalm.views.dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.models.DashBoardModel
import com.example.plazapalm.models.Datam
import com.example.plazapalm.models.GetProfilebyCateResponse
import com.example.plazapalm.models.SelectedDataModel
import com.example.plazapalm.networkcalls.ApiEnums
import com.example.plazapalm.networkcalls.ApiProcessor
import com.example.plazapalm.networkcalls.Repository
import com.example.plazapalm.networkcalls.RetrofitApi
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.CommonMethods.context
import com.example.plazapalm.utils.hideKeyboard
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DashBoardVM @Inject constructor(
    private var repository: Repository,
    var dataStoreUtil: DataStoreUtil,
    var pref: PreferenceFile
) : ViewModel() {
    var selectedEventsList = ArrayList<SelectedDataModel>()
    var recyclerSelectedEvents: RecyclerView? = null
    var token = ObservableField("")
    var C_ID = ObservableField("")
    var status = ObservableField("")
    var lati = ObservableField("")
    var longi = ObservableField("")
    var isClicked: ObservableBoolean = ObservableBoolean(false)

        var dataLista = ArrayList<Datam>()
    var dataList = ArrayList<DashBoardModel>()
    val adapter by lazy { RecyclerAdapter<DashBoardModel>(R.layout.dash_board_items) }
    val openadpter by lazy { RecyclerAdapter<Datam>(R.layout.dash_board_items) }
    val dashSelectedAdapter by lazy { RecyclerAdapter<SelectedDataModel>(R.layout.dash_board_events_items) }

    fun onClicks(view: View) {
        when (view.id) {
            R.id.clDashBoardListSearch -> {
                CommonMethods.showToast(context, "In Progress ")
            }
            R.id.clMainDashBoard -> {
                context.hideKeyboard()
            }
            R.id.ivDashBoardSelectedList -> {
                isClicked.set(true)
                if (isClicked.get()) {
                    showSelectedCatAlert()
                    isClicked.set(false)
                } else {
                    isClicked.set(true)
                }
                //  CommonMethods.showToast(context, "In Progress ")
            }

            R.id.ivDashBoardFilter -> {
                view.navigateWithId(R.id.action_dashBoardFragment_to_filterFragment)
            }
        }
    }

    init {
        Log.e("ASDFASDFSFSDf",status.get().toString())

        token.set(pref.retrieveKey("token"))

        if (status.get().toString().equals("OPEN")) {
            getProfileByCategory()
        } else {
            dataList.add(
                DashBoardModel(
                    "Chris\nEvans",
                    "Android Developer",
                    "Mohali PB ",
                    R.drawable.dash_items_nurse_image,
                    21
                )
            )
            dataList.add(
                DashBoardModel(
                    "Natalie\nPortman",
                    "IOS Developer",
                    "Chandigarh",
                    R.drawable.charis_event_image,
                    22
                )
            )
            dataList.add(
                DashBoardModel(
                    "Robert\nJohnson",
                    "React Native Developer",
                    "Mohali PB",
                    R.drawable.robert_electrician_image,
                    23
                )
            )
            dataList.add(
                DashBoardModel(
                    "Justin\nPottor",
                    "Php Developer",
                    "Mohali PB",
                    R.drawable.justin_carpainter_image,
                    23
                )
            )
            dataList.add(
                DashBoardModel(
                    "Justin\nPottor",
                    "Php Developer",
                    "Mohali PB",
                    R.drawable.justin_carpainter_image,
                    23
                )
            )
            dataList.add(
                DashBoardModel(
                    "Justin\nPottor",
                    "Php Developer",
                    "Mohali PB",
                    R.drawable.justin_carpainter_image,
                    23
                )
            )

            adapter.addItems(dataList)
            adapter.notifyDataSetChanged()

        }



        selectedEventsList.add(SelectedDataModel("Deserts and Drinks"))
        selectedEventsList.add(SelectedDataModel("Events"))
        selectedEventsList.add(SelectedDataModel("Dispensaries"))
        selectedEventsList.add(SelectedDataModel("Local Services"))
        selectedEventsList.add(SelectedDataModel("Pets"))
        dashSelectedAdapter.addItems(selectedEventsList)
        dashSelectedAdapter.notifyDataSetChanged()

        adapter.setOnItemClick { view, _, type ->
            when (type) {
                "dashItemClick" -> {
                    val isDashBoard = Bundle()
                    isDashBoard.putString("comingFrom", "isDashBoard")
                    view.navigateWithId(
                        R.id.action_dashBoardFragment_to_favDetailsFragment,
                        isDashBoard
                    )
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSelectedCatAlert() {
        val dialog: Dialog?
        dialog = Dialog(context, R.style.FullHeightDialog)
        dialog.setContentView(R.layout.dash_board_events_items)
        recyclerSelectedEvents = dialog.findViewById(R.id.rvDashBoardSelectedEvents)
        recyclerSelectedEvents?.layoutManager = LinearLayoutManager(context)
        recyclerSelectedEvents?.adapter = dashSelectedAdapter
        recyclerSelectedEvents?.adapter?.notifyDataSetChanged()
        dialog.setCancelable(true)
        dialog.show()
    }


    fun getProfileByCategory() {

        repository.makeCall(
            ApiEnums.GETPROFILE_BYCATE,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<GetProfilebyCateResponse>> {

                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<GetProfilebyCateResponse> {
                    return retrofitApi.getProfileByCategory(
                        token.get().toString(), C_ID.get().toString(), 1, 5,
                        lati.get()!!.toDouble(), longi.get()!!.toDouble(), "a"
                    )
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(res: Response<GetProfilebyCateResponse>) {
                    if (res.isSuccessful) {
                        if (res.body() != null) {
                            if (res.body()!!.status == 200) {
                                dataLista = res.body()!!.data as ArrayList<Datam>
                                Log.e("QQQAAZZ",dataLista.toString())
                                openadpter.addItems(dataLista)
                                openadpter.notifyDataSetChanged()
                            } else {
                                CommonMethods.showToast(
                                    CommonMethods.context,
                                    res.body()?.message.toString()
                                )
                            }

                        } else {
                            CommonMethods.showToast(
                                CommonMethods.context,
                                res.body()?.message.toString()
                            )
                        }

                    } else {
                        CommonMethods.showToast(
                            CommonMethods.context,
                            res.body()?.message.toString()
                        )
                    }
                }

                override fun onError(message: String) {
                    super.onError(message)
                    Log.e("sdsdsd3", message)
                    CommonMethods.showToast(CommonMethods.context, message)
                }

            }

        )

    }

    private fun dataa(dataList: ArrayList<Datam>) {

    }


}


