package com.example.plazapalm.models

import android.os.Parcel
import android.os.Parcelable

data class GetPostProfileResponse(
    val `data`: postData,
    val message: String,
    val status: Int
)
data class postData(
    val _id: String?,
    val address: String?,
    val booking_status: Boolean?,
    val c_id: String?,
    val category_name: String?,
    val dark_theme: Boolean?,
    val description_1: String?,
    val description_2: String?,
    val description_3: String?,
    val dislikeCount: Int?,
    val distance: Double?,
    val expiry_date: String?,
    val favouriteCount: Int?,
    val first_name: String?,
    val follow: Boolean?,
    val font_size: Int?,
    val isAddedToProfile: Boolean?,
    val isDisliked: Boolean?,
    val isFavourite: Boolean?,
    val isLiked: Boolean?,
    val isPremium: Int?,
    val last_name: String?,
    val lat: Double?,
    val likeCount: Int?,
    val location: getLocation?,
    val location_OnOff: Boolean?,
    val location_text: String?,
    val long: Double?,
    val postProfile_picture: List<Any>?,
    val profile_title: String?,
    val tags: String?,
    val user_id: String?,
    val user_name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        TODO("location"),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readDouble(),
        TODO("postProfile_picture"),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(address)
        parcel.writeByte(if (booking_status!!) 1 else 0)
        parcel.writeString(c_id)
        parcel.writeString(category_name)
        parcel.writeByte(if (dark_theme!!) 1 else 0)
        parcel.writeString(description_1)
        parcel.writeString(description_2)
        parcel.writeString(description_3)
        parcel.writeInt(dislikeCount!!)
        parcel.writeDouble(distance!!)
        parcel.writeString(expiry_date)
        parcel.writeInt(favouriteCount!!)
        parcel.writeString(first_name)
        parcel.writeByte(if (follow!!) 1 else 0)
        parcel.writeInt(font_size!!)
        parcel.writeByte(if (isAddedToProfile!!) 1 else 0)
        parcel.writeByte(if (isDisliked!!) 1 else 0)
        parcel.writeByte(if (isFavourite!!) 1 else 0)
        parcel.writeByte(if (isLiked!!) 1 else 0)
        parcel.writeInt(isPremium!!)
        parcel.writeString(last_name)
        parcel.writeDouble(lat!!)
        parcel.writeInt(likeCount!!)
        parcel.writeByte(if (location_OnOff!!) 1 else 0)
        parcel.writeString(location_text)
        parcel.writeDouble(long!!)
        parcel.writeString(profile_title)
        parcel.writeString(tags)
        parcel.writeString(user_id)
        parcel.writeString(user_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<postData> {
        override fun createFromParcel(parcel: Parcel): postData {
            return postData(parcel)
        }

        override fun newArray(size: Int): Array<postData?> {
            return arrayOfNulls(size)
        }
    }
}

data class getLocation(
    val coordinates: List<Double>,
    val type: String
)