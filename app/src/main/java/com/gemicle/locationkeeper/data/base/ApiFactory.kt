package com.gemicle.locationkeeper.data.base

import com.gemicle.locationkeeper.data.location.LocationApi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiFactory {

    const val CLIENT_ID = "client_id"
    private const val MAIN_API_URL = "http://23.96.123.254:5000"
    private const val SHORT_TIME_OUT = 30

    val apiRequestService: LocationApi =
        getRetrofitDefault(getOkHttpClient())
            .create(LocationApi::class.java)

    private fun getOkHttpClient(timeOut: Int = SHORT_TIME_OUT): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }


    private fun getRetrofitDefault(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAIN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(okHttpClient)
            .build()
    }


}