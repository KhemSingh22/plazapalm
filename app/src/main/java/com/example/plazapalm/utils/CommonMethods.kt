package com.example.plazapalm.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import com.example.plazapalm.models.VerifyData
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream


@SuppressLint("StaticFieldLeak")
object CommonMethods {
    /*This Permission Id for AddCitiesFragment[42]*/
    var pERMISSION_ID = 42

    /***This id is use for AdvanceMap Fragment*/
    var advanceMap_Permission_ID = 45

    /**status bar change according to screen requirement (primary color and white color) */
    fun statusBar(isWhite: Boolean? = false) {
        if (isWhite == true) {
            (MainActivity.context.get() as Activity).setStatusBarColor(R.color.white)
        } else {
            (MainActivity.context.get() as Activity).setStatusBarColor(R.color.app_bar_light)

        }
    }

    var isAdvanceMap: Boolean = false

    /** Common Toast Bar You can use every where in the application using context **/
    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


    var context = (MainActivity.context.get() as Activity)

    private fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }

    /***Delete Alert For Swipe to Delete class ...***/
    var dialog: Dialog? = null

    fun showSwipeDeleteAccountDialog() {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
            dialog = null
        } else {
            dialog = Dialog(context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.delete_swipe_alert)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            dialog?.findViewById<AppCompatTextView>(R.id.tvSwipeDeleteBtn)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.findViewById<AppCompatTextView>(R.id.tvDeleteSwipeCancelBtn)
                ?.setOnClickListener {
                    dialog?.dismiss()
                }
        }

        if (context is AppCompatActivity && !(context as AppCompatActivity).isFinishing && !dialog!!.isShowing) {
            dialog!!.show()
        }
        // dialog?.show()
    }

    /**Open Add question Alert ...***/
    fun openAddQuestionDialog() {
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
            dialog = null
        } else {
            dialog = Dialog(context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.add_questions)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)


            /**choose options click(Button) **/
            dialog?.findViewById<AppCompatButton>(R.id.btnAddedQuesSave)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.findViewById<AppCompatImageView>(R.id.ivAddQuestionCross)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.findViewById<ConstraintLayout>(R.id.clAddQuestion)?.setOnClickListener {
                context.hideKeyboard()
            }
        }
        if (context is AppCompatActivity && !(context as AppCompatActivity).isFinishing && !dialog!!.isShowing) {
            dialog!!.show()
        }

        // dialog?.show()
    }


    fun checkPermissions(activity: Activity, permission: Array<String>): Int {
        var permissionNeeded = 0
        if (Build.VERSION.SDK_INT >= 25) {
            for (i in permission.indices) {
                val result = ContextCompat.checkSelfPermission(activity, permission[i])
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionNeeded++
                }
            }
        }
        return permissionNeeded
    }

    // function to check if GPS is on
    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    lateinit var mFusedLocationClient: FusedLocationProviderClient

    // Get current location, if shifted
    // from previous location
    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    lateinit var currentLocation: LatLng

    // If current location could not be located, use last location
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            currentLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
        }
    }


    // Check if location permissions are
    // granted to the application
    fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    // Request permissions if not granted before
    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            pERMISSION_ID
        )
    }

/*Check camera permission */


    /*   private fun openCamera() {
           val permission = arrayOf(
               android.Manifest.permission.READ_EXTERNAL_STORAGE,
               android.Manifest.permission.WRITE_EXTERNAL_STORAGE
           )
           if (CommonMethods.checkPermissions(context, permission) > 0) {
               ActivityCompat.requestPermissions(context, arrayOf(permission[0], permission[1]), 101)
           } else {
               imageOnClicks.value = true
           }
       }*/


    //  var deviceToken="test55"


    fun saveBitmapToFile(file: File): File? {
        return try {
            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        } catch (e: Exception) {
            null
        }
    }


     fun persistImage(bitmap: Bitmap, name: String) {
        val filesDir: File = context.filesDir
        val imageFile = File(filesDir, "$name.jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        } catch (e: java.lang.Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
    }
    var deviceToken = ObservableField("test55")
    var email = VerifyData().email.toString()
}