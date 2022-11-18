package com.example.plazapalm.views.addphotos.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plazapalm.databinding.AddPhotosItemListBinding
import com.example.plazapalm.interfaces.ItemClickListener
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.networkcalls.IMAGE_LOAD_URL
import java.io.File

class AddPhotosAdapter(
    val activity: FragmentActivity,
    var photos: ArrayList<AddPhoto>,
    var itemClickListener: ItemClickListener
      ) : RecyclerView.Adapter<AddPhotosAdapter.ViewHolder>() {
     var poss : Int? = null
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(picturesList: ArrayList<AddPhoto>, pos: Int) {
        poss = pos
        photos = picturesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layInflater = LayoutInflater.from(parent.context)
        val viewHolder: RecyclerView.ViewHolder?
        viewHolder = ViewHolder(AddPhotosItemListBinding.inflate(layInflater, parent, false))
        return viewHolder

    }
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//       val adapterPhotos=holder.itemView.findViewById<ConstraintLayout>(R.id.clAddPhoto)
//        adapterPhotos.setOnClickListener {
//            itemClickListener.onClick(it,"addPhotos",position)
//        }
//        holder.apply {
//
//            if (photos[position].Image!=null){
//                Glide.with(activity)
//                    .load(photos[position].Image)
//                    .into(dataBinding.setimageView)
//            }
//
//        }
//    }

    override fun getItemCount(): Int = photos.size
    inner class ViewHolder(var bindining: AddPhotosItemListBinding) :
        RecyclerView.ViewHolder(bindining.root) {

        fun setImage(context: Context, photos:ArrayList<AddPhoto>,position:Int
        ) {

            if(photos[position].isValid==true)
            {
                Glide
                    .with(context)
                    .load(IMAGE_LOAD_URL+photos[position].Image)
                    .centerCrop()
                    .into(bindining.setimageView)
            }else
            {
                Glide
                    .with(context)
                    .load(File(photos[position].Image))
                    .centerCrop()
                    .into(bindining.setimageView)
            }


             if (photos[position].Image==""){
                 bindining.plusIcon.visibility = View.VISIBLE
             }else
             {
                 bindining.plusIcon.visibility = View.GONE
             }

        }
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val adapterPhotos=holder.itemView.findViewById<ConstraintLayout>(R.id.clAddPhoto)

        holder.bindining.clAddPhoto.setOnClickListener {
            itemClickListener.onClick(it, "addPhotos", position)
        }
        holder.setImage(activity,photos,position)
    }
}


/*
class AddPhotosVH(var binding: AddPhotosItemListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(position: Int) {
        binding.executePendingBindings()
    }

    fun setImaage() {
//        Glide.with(activity)
//            .load(photos[position].Image)
//            .placeholder(R.drawable.advance_map)
//            .into(binding.setimageView)
    }

}*/
