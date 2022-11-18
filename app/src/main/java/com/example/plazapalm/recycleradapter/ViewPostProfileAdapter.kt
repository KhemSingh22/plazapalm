package com.example.plazapalm.recycleradapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plazapalm.databinding.ViewProfileImagesListBinding
import com.example.plazapalm.models.AddImageDescriptionPOJO
import com.example.plazapalm.networkcalls.IMAGE_LOAD_URL


class ViewPostProfileAdapter(
    var requireActivity: FragmentActivity,
    var dataList: ArrayList<AddImageDescriptionPOJO>
)

    : RecyclerView.Adapter<ViewPostProfileAdapter.ViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layInflater = LayoutInflater.from(parent.context)
        val viewHolder: RecyclerView.ViewHolder?
        viewHolder = ViewHolder(ViewProfileImagesListBinding.inflate(layInflater, parent, false))
        return viewHolder

    }

    override fun getItemCount(): Int = dataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("SDSDSDSDSd",    dataList.toString())

        holder.setDAta(requireActivity, dataList, position)
    }

    inner class ViewHolder(var bindining: ViewProfileImagesListBinding) :
        RecyclerView.ViewHolder(bindining.root) {

        fun setDAta(
            context: Context,
            photos: ArrayList<AddImageDescriptionPOJO>,
            position: Int,
        ) {

            if (!( photos[position].Desc.toString().trim().equals(""))) {
                bindining.clVEditProDescription.visibility = View.VISIBLE
            } else {
                bindining.clVEditProDescription.visibility = View.GONE
            }

            bindining.etVEditProDescription.text = photos[position].Desc.toString().trim()
            Glide.with(context)
                .load(IMAGE_LOAD_URL + photos.get(position).Image)
                .into(bindining!!.ivFavOfDesc1Img)
        }
    }
}


