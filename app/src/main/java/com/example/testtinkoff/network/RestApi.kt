package com.example.testtinkoff.network

import com.example.testtinkoff.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestApi {
    private var retrofit: Retrofit? = null

    private const val BASE_URL = "https://api.tinkoff.ru/"

    fun init () {
        val okHttpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            okHttpClient.addInterceptor(interceptor)
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient.build())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        if (retrofit == null) {
            throw IllegalStateException("Call `RestApi.init(Context, Authenticator)` before calling this method.")
        }
        return retrofit!!.create(serviceClass)
    }
}