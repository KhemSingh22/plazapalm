package com.example.plazapalm.views.myprofile.postprofile.adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plazapalm.databinding.AddImagesViewProfileBinding
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.networkcalls.IMAGE_LOAD_URL
import com.example.plazapalm.views.myprofile.postprofile.PostProfileFragment
import java.io.File


class ViewProAddImageAdapter(
    var context: PostProfileFragment,
    var dataList: ArrayList<AddPhoto>,
   var profileStatus: ObservableBoolean
) : RecyclerView.Adapter<ViewProAddImageAdapter.AddImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddImageViewHolder {
        val layInflater = LayoutInflater.from(parent.context)
//        val viewHolder: RecyclerView.ViewHolder?

        return AddImageViewHolder(AddImagesViewProfileBinding.inflate(layInflater, parent, false))

    }

    override fun onBindViewHolder(holder: AddImageViewHolder, position: Int) {
        holder.setData(dataList[position])
        Log.e("SSSSSBBb","Workinggggggg")

    }

    override fun getItemCount(): Int {

        return dataList.size
    }

  inner class AddImageViewHolder(val binding: AddImagesViewProfileBinding) : RecyclerView.ViewHolder(binding.root) {

      fun setData( img : AddPhoto?) {

          if (img!!.isValid==true){
              Log.e("SSSSSBBb","VVVVCCCCCXXXX")

              Glide.with(context)
                  .load(IMAGE_LOAD_URL+ img!!.Image )
                  .into(binding.ivUsersImage)

              binding.executePendingBindings()
          }else{
              Log.e("SSSSSBBb","WWWSSSSSSS")

              Glide.with(context)
                  .load(File(img.Image))
                  .into(binding.ivUsersImage)

              binding.executePendingBindings()
          }


      }
    }
}