package com.example.plazapalm.views.advancesettings.editfontpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.EditFrontPageFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class EditFrontPageFragment : Fragment(R.layout.edit_front_page_fragment) {


    private var binding: EditFrontPageFragmentBinding? = null
    private val viewModel: EditFrontPageVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditFrontPageFragmentBinding.inflate(layoutInflater)
        //val bottomSheetBehavior = BottomSheetBehavior.from(foo)


        CommonMethods.statusBar(true)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
    }
}