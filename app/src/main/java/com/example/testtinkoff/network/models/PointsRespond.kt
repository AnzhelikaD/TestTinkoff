package com.example.testtinkoff.network.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PointsRespond(
    @SerializedName("resultCode")
    var resultCode: String? = null,
    @SerializedName("payload")
    var payload: List<PayloadModel>? = null,
    @SerializedName("trackingId")
    var trackingId: String? = null
)

data class PayloadModel (
    @SerializedName("externalId")
    var externalId: String? = null,
    @SerializedName("partnerName")
    var partnerName: String? = null,
    @SerializedName("location")
    var location: LocationModel? = null,
    @SerializedName("workHours")
    var workHours: String? = null,
    @SerializedName("addressInfo")
    var addressInfo: String? = null,
    @SerializedName("fullAddress")
    var fullAddress: String? = null,
    @SerializedName("verificationInfo")
    var verificationInfo: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        null,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(externalId)
        parcel.writeString(partnerName)
        parcel.writeString(workHours)
        parcel.writeString(addressInfo)
        parcel.writeString(fullAddress)
        parcel.writeString(verificationInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PayloadModel> {
        override fun createFromParcel(parcel: Parcel): PayloadModel {
            return PayloadModel(parcel)
        }

        override fun newArray(size: Int): Array<PayloadModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class LocationModel(
    @SerializedName("latitude")
    var latitude: Double? = null,
    @SerializedName("longitude")
    var longitude: Double? = null
)