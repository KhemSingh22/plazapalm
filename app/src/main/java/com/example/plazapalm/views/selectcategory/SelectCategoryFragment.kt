package com.example.plazapalm.views.selectcategory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.plazapalm.R
import com.example.plazapalm.databinding.FragmentSelectCategoryBinding
import com.example.plazapalm.interfaces.clickItem
import com.example.plazapalm.views.selectcategory.adapters.SelectCateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryFragment : Fragment(R.layout.fragment_select_category),clickItem {

    private var binding: FragmentSelectCategoryBinding? = null
    private val viewmodel: SelectCategoryViewModel by viewModels()
    lateinit var selectCateAdapter : SelectCateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectCategoryBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewmodel

     getdata()
    }

    private fun getdata() {
        viewmodel.getCategoriesApi(binding!!.rvCategoryLocation,requireActivity(),this)
    }

    override fun click(categoryName: String, position: Int, c_id: String?, s: String) {
//        requireView().findNavController(R.id.action_bookingDetailsFragment_to_postProfileFragment)
        Log.e("ASaaa", categoryName+"DDDDDDDDDDDD" + position.toString())
        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", categoryName +"/"+ c_id)
        findNavController().popBackStack()

    }

}
 