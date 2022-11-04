package com.example.plazapalm.views.advancesettings.pictures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.PicturesFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class PicturesFragment : Fragment(R.layout.pictures_fragment) {
    private var binding: PicturesFragmentBinding? = null
    private val viewModel: PicturesVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PicturesFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
        setPicturesAdapter()
    }
    private fun setPicturesAdapter() {
        binding?.rvPictures?.layoutManager = GridLayoutManager(requireContext(), 3)
        binding?.rvPictures?.adapter = viewModel.picturesAdapter
        binding?.rvPictures?.adapter?.notifyDataSetChanged()
    }
}