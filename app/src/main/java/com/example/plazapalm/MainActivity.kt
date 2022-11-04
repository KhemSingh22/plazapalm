package com.example.plazapalm

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.plazapalm.databinding.ActivityMainBinding
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.hideKeyboard
import com.example.plazapalm.utils.onNavDestinationSelected
import com.example.plazapalm.views.MainVM
import com.example.plazapalm.views.catergorylist.CategoriesListFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val mainVM: MainVM? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var navController: NavController? = null
    companion object {
        // var navListener: NavigationListener? = null
        lateinit var context: WeakReference<Context>
        lateinit var activity: Activity
    }
    private var binding: ActivityMainBinding? = null
    override fun onStart() {
        super.onStart()
        context = WeakReference(this)
        activity = Activity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding?.model = mainVM
        activity = MainActivity()
        mainVM?.navController = findNavController(R.id.fragmentMain)
        context = WeakReference(this)
        setUpNav()
        //setTypeHeretoCategoryFragment()
        val currentNightMode = Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // Night mode is not active, we're using the light theme
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                // Night mode is active, we're using dark theme
            }
        }

    }
    private fun setUpNav() {
        navController = findNavController(R.id.fragmentMain)
        binding?.bottNavMain?.setOnNavigationItemSelectedListener(this)
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.dashBoardFragment || destination.id == R.id.myProfileFragment || destination.id == R.id.messagesFragment || destination.id == R.id.openCategeroyFragment || destination.id == R.id.advanceMapFragment || destination.id == R.id.picturesFragment) {
                binding?.bottNavMain?.selectedItemId = destination.id
                binding?.bottNavMain?.isVisible = true
            } else {
                binding?.bottNavMain?.isVisible = false
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId != navController!!.currentDestination!!.id) {
            when (item.itemId) {
                R.id.dashBoard -> {
                    hideKeyboard()
                    binding?.bottNavMain?.onNavDestinationSelected(item.itemId, navController!!)
                    navController?.navigate(R.id.dashBoardFragment)
                }
                R.id.categories -> {
                    hideKeyboard()
                    binding?.bottNavMain?.onNavDestinationSelected(item.itemId, navController!!)
                    var bundle=Bundle()
                    bundle.putString("comingFrom","main")
                     navController?.navigate(R.id.openCategeroyFragment,bundle)
//                    navController?.navigate(R.id.categoriesListFragment,bundle)

                }
                R.id.messages -> {
                    hideKeyboard()
                    binding?.bottNavMain?.onNavDestinationSelected(item.itemId, navController!!)
                    navController?.navigate(R.id.messagesFragment)

                }
                R.id.myProfile -> {
                    hideKeyboard()
                    binding?.bottNavMain?.onNavDestinationSelected(item.itemId, navController!!)
                    navController?.navigate(R.id.myProfileFragment)

                }
                else -> {

                }
            }
        }
        return true
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            showExitDialog()
        } else {
            super.onBackPressed()
        }
    }

    var dialog: Dialog? = null
    private fun showExitDialog() {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(context.get()!!, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.alert_exit)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(true)

            dialog?.findViewById<TextView>(R.id.tvCancel)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.findViewById<TextView>(R.id.tvYes)?.setOnClickListener {
                finishAffinity()
                dialog?.dismiss()
            }
        }
        dialog?.show()
    }

    private fun setTypeHeretoCategoryFragment() {
        val intent = Intent(this, CategoriesListFragment::class.java)
        val bundle = Bundle()
        bundle.putString("comingFrom", "mainActivity")
        activity.startActivity(intent, bundle)
    }


}