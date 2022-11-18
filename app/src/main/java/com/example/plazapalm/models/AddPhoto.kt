package com.example.plazapalm.models

import android.os.Parcel
import android.os.Parcelable

data class AddPhoto(var Image : String? = "",var isValid:Boolean?=false) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddPhoto> {
        override fun createFromParcel(parcel: Parcel): AddPhoto {
            return AddPhoto(parcel)
        }

        override fun newArray(size: Int): Array<AddPhoto?> {
            return arrayOfNulls(size)
        }
    }
}
