package com.example.plazapalm.views.myprofile.postprofile.adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plazapalm.databinding.AddImagesViewProfileBinding
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.views.myprofile.postprofile.PostProfileFragment


class ViewProAddImageAdapter(var context: PostProfileFragment , var dataList: ArrayList<String>) : RecyclerView.Adapter<ViewProAddImageAdapter.AddImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddImageViewHolder {
        val layInflater = LayoutInflater.from(parent.context)
//        val viewHolder: RecyclerView.ViewHolder?

        return AddImageViewHolder(AddImagesViewProfileBinding.inflate(layInflater, parent, false))

    }

    override fun onBindViewHolder(holder: AddImageViewHolder, position: Int) {
        holder.setData(dataList[position].toString())
        Log.e("SSSSSBBb","Workinggggggg")

    }

    override fun getItemCount(): Int {

        return dataList.size
    }

    /*inner class AddImagesVH(val binding: AddImagesViewProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }*/

  inner class AddImageViewHolder(val binding: AddImagesViewProfileBinding) : RecyclerView.ViewHolder(binding.root) {

      fun setData( img : String?) {

              Glide.with(context)
                  .load(img)
                  .into(binding.ivUsersImage)

              binding.executePendingBindings()

      }
    }
}