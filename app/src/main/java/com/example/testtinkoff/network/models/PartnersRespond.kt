package com.example.testtinkoff.network.models

import com.google.gson.annotations.SerializedName

data class PartnersRespond(
    @SerializedName("resultCode")
    var resultCode: String? = null,
    @SerializedName("payload")
    var payload: List<PartnerModel>? = null,
    @SerializedName("trackingId")
    var trackingId: String? = null
)

data class PartnerModel (
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("picture")
    var picture: String? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("hasLocations")
    var hasLocations: Boolean? = null,
    @SerializedName("isMomentary")
    var isMomentary: Boolean? = null,
    @SerializedName("depositionDuration")
    var depositionDuration: String? = null,
    @SerializedName("limitations")
    var limitations: String? = null,
    @SerializedName("pointType")
    var pointType: String? = null,
    @SerializedName("externalPartnerId")
    var externalPartnerId: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("moneyMin")
    var moneyMin: Int? = null,
    @SerializedName("moneyMax")
    var moneyMax: Long? = null,
    @SerializedName("hasPreferentialDeposition")
    var hasPreferentialDeposition: Boolean? = null,
    @SerializedName("limits")
    var limits: List<Limit>? = null,
    @SerializedName("dailyLimits")
    var dailyLimits: List<DailyLimit>? = null
)

data class Limit(
    @SerializedName("currency")
    var currency: Currency? = null,
    @SerializedName("min")
    var min: Int? = null,
    @SerializedName("max")
    var max: Long? = null
)

data class Currency(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("strCode")
    var strCode: String? = null
)

data class DailyLimit(
    @SerializedName("currency")
    var currency: Currency? = null,
    @SerializedName("amount")
    var amount: Long? = null
)