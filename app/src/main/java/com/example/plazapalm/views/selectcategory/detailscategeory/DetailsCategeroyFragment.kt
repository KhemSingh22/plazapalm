package com.example.plazapalm.views.selectcategory.detailscategeory

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.DashBoardFragmentBinding
import com.example.plazapalm.databinding.FragmentDetailsCategeroyBinding
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.views.dashboard.DashBoardVM


class DetailsCategeroyFragment : Fragment(R.layout.fragment_details_categeroy) {
    private var binding: FragmentDetailsCategeroyBinding? = null
    private val viewModel: DashBoardVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsCategeroyBinding.inflate(layoutInflater)
        return binding!!.root
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
        binding?.rvDashBoard?.layoutManager= GridLayoutManager(requireContext(),2)
        binding?.rvDashBoard?.adapter?.notifyDataSetChanged()
    }
}