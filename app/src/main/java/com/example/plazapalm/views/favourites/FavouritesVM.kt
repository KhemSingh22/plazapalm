package com.example.plazapalm.views.favourites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.models.DashBoardModel
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("NotifyDataSetChanged")
@HiltViewModel
class FavouritesVM @Inject constructor() : ViewModel() {
    var favouritesList = ArrayList<DashBoardModel>()
    val favAdapter by lazy { RecyclerAdapter<DashBoardModel>(R.layout.favourites_list_items) }
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivFavBackBtn -> {
                view.findNavController().navigateUp()
            }
        }

    }
    init {
        favouritesList.add(
            DashBoardModel(
                "Vikas",
                "Android Developer",
                "Mohali PB",
                R.drawable.charis_event_image,
                0
            )
        )
        favouritesList.add(
            DashBoardModel(
                "Prince",
                "Android Developer",
                "Ludhiana PB",
                R.drawable.dash_items_nurse_image,
                2
            )
        )
        favouritesList.add(
            DashBoardModel(
                "Demo",
                "Android Developer",
                "Chandigarh PB",
                R.drawable.charis_event_image,
                3
            )
        )
        favouritesList.add(
            DashBoardModel(
                "Kotlin",
                "Android Developer",
                "Delhi DL",
                R.drawable.charis_event_image,
                5
            )
        )
        favAdapter.addItems(favouritesList)
        favAdapter.notifyDataSetChanged()



        favAdapter.setOnItemClick { view, _, type ->
            when (type) {
                "favDetailsItem" -> {
                    val isFavorite=Bundle()
                    isFavorite.putString("comingFrom","isFavorite")
                    view.navigateWithId(R.id.action_favouritesFragment_to_favDetailsFragment,isFavorite)
                }
            }
        }


    }
}

