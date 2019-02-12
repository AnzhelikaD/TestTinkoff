package com.example.testtinkoff.utils

import android.util.DisplayMetrics

object DisplayManager {

    private const val ICON_URL_XHDPI = "https://static.tinkoff.ru/icons/deposition-partners-v3/xhdpi/"
    private const val ICON_URL_HDPI = "https://static.tinkoff.ru/icons/deposition-partners-v3/hdpi/"
    private const val ICON_URL_MDPI = "https://static.tinkoff.ru/icons/deposition-partners-v3/mdpi/"
    private const val ICON_URL_LDPI = "https://static.tinkoff.ru/icons/deposition-partners-v3/ldpi/"

    var appDensityDpi: Int? = null

    fun getBasePictureUrl() : String {
        return when (appDensityDpi) {
            DisplayMetrics.DENSITY_LOW -> ICON_URL_LDPI
            DisplayMetrics.DENSITY_MEDIUM -> ICON_URL_MDPI
            DisplayMetrics.DENSITY_HIGH -> ICON_URL_HDPI
            DisplayMetrics.DENSITY_XHIGH -> ICON_URL_XHDPI
            else -> ICON_URL_MDPI
        }
    }
}