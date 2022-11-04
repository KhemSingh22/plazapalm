package com.example.plazapalm.views.messageslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.MessagesFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MessagesFragment : Fragment(R.layout.messages_fragment) {
    private var binding:MessagesFragmentBinding?=null
    private val viewModel:MessagesVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=MessagesFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm=viewModel
        setAdapter()
    }

    private fun setAdapter(){
        binding?.rvMessages?.layoutManager=LinearLayoutManager(requireContext())
        binding?.rvMessages?.adapter=viewModel.messageUserAdapter
        binding?.rvMessages?.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(true)
    }

}