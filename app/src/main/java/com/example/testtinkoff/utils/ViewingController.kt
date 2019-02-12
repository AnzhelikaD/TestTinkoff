package com.example.testtinkoff.utils

import android.util.Log

object ViewingController {
    private const val VIEWED_POINTS_KEY = "view_points_key"

    private var viewedPointsIds: ArrayList<String>? = null

    fun addPoint(id: String) {
        if (viewedPointsIds == null) {
            viewedPointsIds = Prefs.load<ArrayList<String>>(VIEWED_POINTS_KEY, ArrayList::class.java) ?: ArrayList()
        }

        if (viewedPointsIds?.contains(id) != true) {
            viewedPointsIds?.add(id)
            Prefs.save(VIEWED_POINTS_KEY, viewedPointsIds)
        }

        Log.w("Prefs ", viewedPointsIds.toString())
    }

    fun contains(id: String?) : Boolean {
        if (viewedPointsIds == null) {
            viewedPointsIds = Prefs.load<ArrayList<String>>(VIEWED_POINTS_KEY, ArrayList::class.java) ?: ArrayList()
        }
        return viewedPointsIds?.contains(id) ?: false
    }

    fun clear() {
        Prefs.remove(VIEWED_POINTS_KEY)
    }
}