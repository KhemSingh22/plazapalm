package com.example.plazapalm.views.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.plazapalm.R
import com.example.plazapalm.models.FilterDataModel
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("NotifyDataSetChanged")
@HiltViewModel
class FilterFragmentVM @Inject constructor():ViewModel() {
    var miles=ObservableField("")
    var filterDataList=ArrayList<FilterDataModel>()
    val filterAdapter by lazy { RecyclerAdapter<FilterDataModel>(R.layout.filter_catergories_item) }

    init {
        filterDataList.add(FilterDataModel("Plumber"))
        filterDataList.add(FilterDataModel("Doctor"))
        filterDataList.add(FilterDataModel("Carpenter"))
        filterDataList.add(FilterDataModel("Construction"))
        filterDataList.add(FilterDataModel("Tailor"))
        filterDataList.add(FilterDataModel("Security Guard"))
        filterDataList.add(FilterDataModel("Electrician"))
        filterAdapter.addItems(filterDataList)
        filterAdapter.notifyDataSetChanged()
    }

    fun onClicks(view:View){
        when(view.id){
            R.id.ivFilterBackButton->{
                view.findNavController().navigateUp()
            }
            R.id.tvFilterAddCategories->{
                val bundle= Bundle()
                bundle.putString("comingFrom","isFilter")
                view.navigateWithId(R.id.action_filterFragment_to_categoriesListFragment,bundle)
            }
        }
    }
}