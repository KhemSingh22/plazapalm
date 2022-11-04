package com.example.plazapalm.views.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plazapalm.R
import com.example.plazapalm.databinding.ChatFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {
    private var binding: ChatFragmentBinding? = null
    val viewModel: ChatVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChatFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm=viewModel
       // openUserBlockButton()
        setAdapter()
    }

    /** Here Set Chat Adapter in on Created method **/
    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(){
        binding?.rvChats?.layoutManager=LinearLayoutManager(requireContext())
        binding?.rvChats?.adapter=viewModel.chatAdapter
        binding?.rvChats?.adapter?.notifyDataSetChanged()
    }

    private fun openUserBlockButton(){
        if (viewModel.isClicked.get())
        {
            binding?.tvBlockUserBtn?.visibility=View.VISIBLE
        }
        else
        {
            binding?.tvBlockUserBtn?.visibility=View.GONE

        }

    }

}