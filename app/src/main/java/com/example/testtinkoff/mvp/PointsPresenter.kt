package com.example.testtinkoff.mvp

import com.example.testtinkoff.utils.RxBus
import com.example.testtinkoff.network.RestApi
import com.example.testtinkoff.network.models.PartnerModel
import com.example.testtinkoff.network.models.PayloadModel
import com.example.testtinkoff.network.services.PointsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import java.util.*

object PointsPresenter {
   private const val TAG = "PointsPresenter"

    private val subscriptions = CompositeDisposable()
    private val requestQueue = HashSet<Call<*>>()

    private val service = RestApi.createService(PointsService::class.java)

    private var points: List<PayloadModel>? = null

    private var partners: List<PartnerModel>? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    data class PointsLoadedEvent(val points: List<PayloadModel>?)
    val pointsLoadedBus = RxBus<PointsLoadedEvent>()

    fun loadPoints(latitude: Double?, longitude: Double?, radius: Long?) {

        unsubscribeOnDestroy(service.getAllPoints(latitude, longitude, radius)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (partners == null) {
                    getPartners()
                }

                pointsLoadedBus.send(PointsLoadedEvent(it.payload))
                points = it.payload

            }, {
                it.printStackTrace()
                pointsLoadedBus.send(PointsLoadedEvent(null))
            }))
    }

    fun getPartners() {
        unsubscribeOnDestroy(service.getPartners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                partners = it.payload

            }, {
                it.printStackTrace()
            }))
    }

    fun getPartner(partnerName: String) : PartnerModel? {
        if (!partners.isNullOrEmpty()) {
            for (partner in partners!!) {
                if (partnerName == partner.id) {
                    return partner
                }
            }
        } else {
            getPartners()
        }
        return null
    }

    fun getPoints(): List<PayloadModel>? {
        return points
    }

    private fun unsubscribeOnDestroy(disposable: Disposable) {
        subscriptions.add(disposable)
    }

    fun onDestroyView() {
        subscriptions.clear()

        if (requestQueue.isEmpty()) {
            return
        }

        for (call in requestQueue) {
            call.cancel()
        }

        requestQueue.clear()
    }
}