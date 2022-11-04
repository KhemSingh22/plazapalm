package com.example.plazapalm.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.plazapalm.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    // Check if no view has focus
    val currentFocusedView = currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Activity.getViewHeightFromBottom(view: View): Int {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    val loc = IntArray(2)
    view.getLocationOnScreen(loc)
    val distance = dm.heightPixels - loc[1]
    return distance - 1000
}


fun Activity.printHashKey() {
    try {
        // Get key hash of local SHA1 key
        val info = packageManager.getPackageInfo(
            applicationContext.packageName,
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    } catch (e: PackageManager.NameNotFoundException) {

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    // Get key hash of play store SHA1 key
/*    val sha1 = "C4:67:EB:34:87:8E:84:80:72:64:0F:3E:6A:A2:D5:5C:44:A4:D3:2F"
    val arr: List<String> = sha1.split(":")
    val byteArr = ByteArray(arr.size)

    for (i in arr.indices) {
        byteArr[i] = Integer.decode("0x" + arr[i]).toByte()
    }

    Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP))*/
}


fun BottomNavigationView.onNavDestinationSelected(
    itemId: Int,
    navController: NavController
): Boolean {
    val builder = NavOptions.Builder()
        .setLaunchSingleTop(true)

    if (navController.currentDestination!!.parent!!.findNode(itemId) is ActivityNavigator.Destination) {
//        builder.setEnterAnim(R.anim.fade_in)
//            .setExitAnim(R.anim.fade_out)
//            .setPopEnterAnim(R.anim.fade_in)
//            .setPopExitAnim(R.anim.fade_out)
    } else {
//        builder.setEnterAnim(R.anim.fade_in)
//            .setExitAnim(R.anim.fade_out)
//            .setPopEnterAnim(R.anim.fade_in)
//            .setPopExitAnim(R.anim.fade_out)
    }
    //if (itemId == getChildAt(0).id) {
    //builder.setPopUpTo(findStartDestination(navController.graph)!!.id, true)
    // }
    builder.setPopUpTo(itemId, true)
    val options = builder.build()
    return try {
        //TODO provide proper API instead of using Exceptions as Control-Flow.
        navController.navigate(itemId, null, options)
        true
    } catch (e: IllegalArgumentException) {
        false
    }




}