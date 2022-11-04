package com.example.plazapalm.views.favourites.favdetails

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.models.ImagesVideosModel
import com.example.plazapalm.models.PicturesModel
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FavDetailsVM @Inject constructor() : ViewModel() {


    var uploadImagesList=ArrayList<ImagesVideosModel>()
    var uploadVideoList=ArrayList<ImagesVideosModel>()
    val imagesAdapter by lazy { RecyclerAdapter<ImagesVideosModel>(R.layout.view_profile_images_list) }
    val videosAdapter by lazy { RecyclerAdapter<ImagesVideosModel>(R.layout.view_profile_videos_list) }
    var isFavourites = ObservableBoolean(false)
    var dialog: Dialog? = null





    init {

        uploadImagesData()
        uploadVideosData()
    }
    fun onClicks(view: View) {
        when (view.id) {
            R.id.ivFavDetailsBackBtn -> {
                view.findNavController().navigateUp()
            }

            R.id.ivFavDetailsOptions -> {

                showFavDetailsDialog(view)

                /*  if (isPremium){
                      dialogPremiumEditDeleteProfile(view)
                  }
                  else{

                  }*/

            }

            R.id.ivFavDetailsChats -> {
                view.navigateWithId(R.id.action_favDetailsFragment_to_chatFragment)
            }

            R.id.btnBookingProfile -> {
                view.navigateWithId(R.id.confirmBookingFragment)
            }
        }
    }


    private fun showFavDetailsDialog(view: View) {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.fav_details_choose_options)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            /**choose options click(Button) **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptionTitle)?.setOnClickListener {
                dialog?.dismiss()
            }
            /**Remove From Favourites Click (Button)**/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptRemoveFavDetails)
                ?.setOnClickListener {
                    dialog?.dismiss()
                }
            /** Add to Calendar (Button) **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptAddToCalFavDetails)
                ?.setOnClickListener {
                    dialog?.dismiss()
                }
            /** Share Click (Button) **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvFavChooseShare)?.setOnClickListener {
                dialog?.dismiss()
            }

            /**RePort Button Click**/
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseOptFavDetailsReport)
                ?.setOnClickListener {
                    view.navigateWithId(R.id.action_favDetailsFragment_to_reportChooseOptionFragment)
                    dialog?.dismiss()
                }
            /*** Cancel Button Clicks **/
            dialog?.findViewById<AppCompatTextView>(R.id.tvFavDetailsCancel)
                ?.setOnClickListener {
                    dialog?.dismiss()
                }
        }
        dialog?.show()
    }


    private fun uploadImagesData(){
        uploadImagesList.add(ImagesVideosModel(R.drawable.electrician_image,""))
        uploadImagesList.add(ImagesVideosModel(R.drawable.electrician_image,""))
        uploadImagesList.add(ImagesVideosModel(R.drawable.electrician_image,""))
        uploadImagesList.add(ImagesVideosModel(R.drawable.electrician_image,""))
        uploadImagesList.add(ImagesVideosModel(R.drawable.electrician_image,""))
        imagesAdapter.addItems(uploadImagesList)
        imagesAdapter.notifyDataSetChanged()
    }
    private fun uploadVideosData(){
        uploadVideoList.add(ImagesVideosModel(0,"https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1"))
        uploadVideoList.add(ImagesVideosModel(0,"https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1"))
        uploadVideoList.add(ImagesVideosModel(0,"https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1"))
        uploadVideoList.add(ImagesVideosModel(0,"https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1"))
        videosAdapter.addItems(uploadVideoList)
        videosAdapter.notifyDataSetChanged()
    }

    private fun dialogPremiumEditDeleteProfile(view: View) {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.edit_or_delete_profile)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
        }
        dialog?.findViewById<AppCompatTextView>(R.id.tvEditProPremiumOpt)?.setOnClickListener {
            view.navigateWithId(R.id.postProfileFragment)
        }
        dialog?.findViewById<AppCompatTextView>(R.id.tvEditProPremiumDelete)?.setOnClickListener {

            dialog?.dismiss()
        }
        dialog?.findViewById<AppCompatTextView>(R.id.tvEditProPremiumCancel)?.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

}