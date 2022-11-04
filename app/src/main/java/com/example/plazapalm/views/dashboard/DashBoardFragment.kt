package com.example.plazapalm.views.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.plazapalm.R
import com.example.plazapalm.databinding.DashBoardFragmentBinding
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : Fragment(R.layout.dash_board_fragment) {
    private var binding : DashBoardFragmentBinding? = null
    private val viewModel : DashBoardVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashBoardFragmentBinding.inflate(layoutInflater)
        getdata()

        return binding?.root
    }

    private fun getdata() {
        if (arguments?.getString("fromOpencate")!=null){
            binding!!.ivDashBoardFilter.visibility =View.GONE
            binding!!.searchBar.visibility =View.GONE
            binding!!.ivDashBoardSelectedList.visibility =View.GONE

            binding!!.tvDashBoardTitle.text=arguments?.getString("cateName")
            viewModel.C_ID.set(arguments?.getString("c_id"))
            viewModel.longi.set(arguments?.getString("longitude"))
            viewModel.lati.set(arguments?.getString("latitude"))

            viewModel.status.set(arguments?.getString("status"))


//            Glide.with(this)
//                .load(R.drawable.ic_back_icon_black)
//                .into(binding!!.ivDashBoardSelectedList)

        }else{
            viewModel.status.set("DashBoard")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        binding?.vm = viewModel

    }

    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(){
        binding?.rvDashBoard?.layoutManager=GridLayoutManager(requireContext(),2)
        binding?.rvDashBoard?.adapter?.notifyDataSetChanged()

    }

}