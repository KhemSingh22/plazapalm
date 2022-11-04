package com.example.plazapalm.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AppController : Application() {
    @Inject
    lateinit var dataStoreUtil: DataStoreUtil

    companion object {
        var appContext: Context? = null
        const val LANG = "en"
        var language = "en"
        var CURRENT_LAT: Double = 0.0
        var CURRENT_LONG: Double = 0.0
        var CURRENT_ADDRESS: String = ""
        var LOC_ACCURACY: String = ""


    }


/*    override fun onCreate() {
        super.onCreate()
        // Change language here
        dataStoreUtil.readData(LANGUAGE){ lang ->
            if (lang != null) {
                language = lang
                MainActivity.context.get()!!.changeLang(lang)
            }
        }
        appContext = applicationContext
//        dataStoreUtil.readObject(LOGIN_DATA, SignUpData::class.java) { data ->
//            SocketHelper.token = data?.token ?: ""
//            SocketHelper.initSocket()
//            data
//        }
        Log.d("appController", "calls")
    }*/


}