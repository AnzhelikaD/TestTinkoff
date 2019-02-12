package com.example.testtinkoff.network.services

import com.example.testtinkoff.network.models.PartnersRespond
import com.example.testtinkoff.network.models.PointsRespond
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsService {
    @GET("v1/deposition_points")
    fun getAllPoints(@Query("latitude") latitude: Double?,
                     @Query("longitude") longitude: Double?,
                     @Query("radius") radius: Long? = 1000) : Observable<PointsRespond>

    @GET("https://api.tinkoff.ru/v1/deposition_partners")
    fun getPartners(@Query("accountType") accountType: String? = "Credit") : Observable<PartnersRespond>
}