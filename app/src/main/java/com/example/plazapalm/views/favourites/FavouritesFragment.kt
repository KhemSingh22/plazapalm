package com.example.plazapalm.views.favourites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FavouritesFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import com.google.android.gms.common.internal.service.Common
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.favourites_fragment) {
    private var binding: FavouritesFragmentBinding? = null
    private val viewModel: FavouritesVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouritesFragmentBinding.inflate(layoutInflater)
        CommonMethods.statusBar(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
        setFavouritesAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setFavouritesAdapter() {
        binding?.rvFavourites?.layoutManager = GridLayoutManager(requireContext(), 2)
        binding?.rvFavourites?.adapter = viewModel.favAdapter
        binding?.rvFavourites?.adapter?.notifyDataSetChanged()
    }
}