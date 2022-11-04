package com.example.plazapalm.views.messageslist

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.plazapalm.R
import com.example.plazapalm.models.UserMessageModal
import com.example.plazapalm.recycleradapter.RecyclerAdapter
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesVM @Inject constructor():ViewModel() {

    var isVisible =ObservableBoolean(false)
    var usersList=ArrayList<UserMessageModal>()
    val messageUserAdapter by lazy { RecyclerAdapter<UserMessageModal>( R.layout.messages_user_list) }

    /**Implemented clicks Here**/
    fun onClicks(view:View){
        when(view.id){
        }
    }

    init {
        usersList.add(UserMessageModal(R.drawable.chat_user_girl,"Nguyen Shane","02:30 PM","There's somebody out there ..."))
        usersList.add(UserMessageModal(R.drawable.cooper_image,"Cooper Kristin","02:30 PM","There's somebody out there ..."))
        usersList.add(UserMessageModal(R.drawable.naguyen_girl,"Nguyen Shane","02:30 PM","There's somebody out there ..."))
        usersList.add(UserMessageModal(R.drawable.flores_image,"Flores Juanita","02:30 PM","There's somebody out there ..."))
        usersList.add(UserMessageModal(R.drawable.williamson_dog,"Williamson","02:30 PM","There's somebody out there ..."))
        usersList.add(UserMessageModal(R.drawable.richard_img,"Richard Aubrey","02:30 PM","There's somebody out there ..."))
        messageUserAdapter.addItems(usersList)
        messageUserAdapter.notifyDataSetChanged()

        messageUserAdapter.setOnItemClick { view, position, type ->
            when (type) {
                "userChatDetails" -> {
              view.navigateWithId(R.id.action_messagesFragment_to_chatFragment)
                }
            }
        }

    }
     fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }


}