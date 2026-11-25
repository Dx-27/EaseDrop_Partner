package com.example.easedroppartner.easedroppartner.Model

import android.os.Parcel
import android.os.Parcelable

class All_India_Delivery_Model() : Parcelable {
    var id: String? = null
    var cid: String? = null
    var sender_name: String? = null
    var sender_ph: String? = null
    var sender_add: String? = null
    var sender_lat_lang: String? = null
    var receiver_name: String? = null
    var receiver_ph: String? = null
    var receiver_add: String? = null
    var package_desc: String? = null
    var package_weight: String? = null
    var is_fragile: String? = null
    var pickup_date: String? = null
    var pickup_time: String? = null
    var payment_type: String? = null
    var total_amt: String? = null
    var d_status: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        cid = parcel.readString()
        sender_name = parcel.readString()
        sender_ph = parcel.readString()
        sender_add = parcel.readString()
        sender_lat_lang = parcel.readString()
        receiver_name = parcel.readString()
        receiver_ph = parcel.readString()
        receiver_add = parcel.readString()
        package_desc = parcel.readString()
        package_weight = parcel.readString()
        is_fragile = parcel.readString()
        pickup_date = parcel.readString()
        pickup_time = parcel.readString()
        payment_type = parcel.readString()
        total_amt = parcel.readString()
        d_status = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(cid)
        parcel.writeString(sender_name)
        parcel.writeString(sender_ph)
        parcel.writeString(sender_add)
        parcel.writeString(sender_lat_lang)
        parcel.writeString(receiver_name)
        parcel.writeString(receiver_ph)
        parcel.writeString(receiver_add)
        parcel.writeString(package_desc)
        parcel.writeString(package_weight)
        parcel.writeString(is_fragile)
        parcel.writeString(pickup_date)
        parcel.writeString(pickup_time)
        parcel.writeString(payment_type)
        parcel.writeString(total_amt)
        parcel.writeString(d_status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<All_India_Delivery_Model> {
        override fun createFromParcel(parcel: Parcel): All_India_Delivery_Model {
            return All_India_Delivery_Model(parcel)
        }

        override fun newArray(size: Int): Array<All_India_Delivery_Model?> {
            return arrayOfNulls(size)
        }
    }
}