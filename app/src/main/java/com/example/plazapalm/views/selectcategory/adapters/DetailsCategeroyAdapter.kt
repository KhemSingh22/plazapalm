package com.example.plazapalm.views.selectcategory.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.plazapalm.databinding.SlectCateItemsBinding
import com.example.plazapalm.interfaces.clickItem
import com.example.plazapalm.models.CategoriesData

class DetailsCategeroyAdapter(
    var requireActivity: FragmentActivity,
    var photos: List<CategoriesData>,
    var clickItem: clickItem,
    var status : String
) :
    RecyclerView.Adapter<DetailsCategeroyAdapter.SelectCateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCateViewHolder {
        val layInflater = LayoutInflater.from(parent.context)
        val viewHolder: RecyclerView.ViewHolder?
        viewHolder = SelectCateViewHolder(SlectCateItemsBinding.inflate(layInflater, parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: SelectCateViewHolder, position: Int) {

        if (status.equals("selectcategeroy")){
            holder.binding.tvSelectCategory.text = photos[position].category_name + " (" + photos[position].count+")"

            holder.binding.tvSelectCategory.setOnClickListener {
                clickItem.click(photos[position].category_name,position,photos[position]._id,"postprofile")
            }

        }else if (status.equals("OpenCategeroy")){
            holder.binding.tvSelectCategory.text = photos[position].category_name + " (" + photos[position].count+")"
            holder.binding.tvSelectCategory.setOnClickListener {
                clickItem.click(photos[position].category_name,position,photos[position]._id,"OpenCategeroy")
            }

        }

    }

    override fun getItemCount(): Int {
        return photos.size
    }

    class SelectCateViewHolder(var binding: SlectCateItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}