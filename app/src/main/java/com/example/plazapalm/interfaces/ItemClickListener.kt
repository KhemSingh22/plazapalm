package com.example.plazapalm.interfaces

import android.view.View

interface ItemClickListener {
    fun onClick(view: View,type: String, position: Int)
}