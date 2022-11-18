package com.example.plazapalm.pref

import android.content.SharedPreferences
import com.example.plazapalm.models.AddPhoto
import com.example.plazapalm.models.SavePostProfileResponse
import com.example.plazapalm.utils.Constants
import com.google.gson.Gson
import javax.inject.Inject

class PreferenceFile @Inject constructor(
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
 ) {
    fun storeKey(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun storeBoolKey(key : String, value : Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun retrieveBoolKey(key: String): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    fun storeLocarion(value: String) {
        editor.putString("location", value)
        editor.apply()
    }
    fun storeResponse( body: SavePostProfileResponse) {
        val gson = Gson()
        val json: String = gson.toJson(body)
        editor.putString(Constants.POSTRESPONSE, json)
        editor.apply()
    }

    fun retrieveResponse(): SavePostProfileResponse? {
        val json : String?  = sharedPreferences.getString(Constants.POSTRESPONSE, "{}" )
//        val obj: SavePostProfileResponse = gson.fromJson(json, SavePostProfileResponse::class.java)
        return Gson().fromJson(json, SavePostProfileResponse::class.java)

    }
    fun retrieveLocarion() : String? {
        return sharedPreferences.getString("location", null)
    }

    fun retrieveKey(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun storeImage(key : String, value : ArrayList<AddPhoto>) {
        val gson = Gson()
        val listValue = gson.toJson(value)
        editor.putString(key, listValue)
        editor.apply()
    }

    fun retrieveImage(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun <T> storeObject(key: String, data: T) {
        editor.putString(key, Gson().toJson(data))
        editor.apply()
    }


    fun clearPreference() {
        editor.clear()
        editor.apply()
//    openActivity(Intent(this, Login::class.java), true)
    }

/*
    fun retrieveLoginData(): LoginResponse {

        return Gson().fromJson(
            sharedPreferences.getString(loginDataKey, "")!!,
            LoginResponse::class.java
        )
    }
*/
}