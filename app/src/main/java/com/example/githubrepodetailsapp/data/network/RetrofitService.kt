package com.avatar.inpsection.data.network

import android.annotation.SuppressLint
import android.content.Context
import com.example.githubrepodetailsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by ramesh on 20-07-2021
 */
@SuppressLint("StaticFieldLeak")
object RetrofitService {
    var context: Context? = null
    var httpBuilder: OkHttpClient =OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor())
            .connectTimeout(90, TimeUnit.SECONDS)
            .addInterceptor((if(BuildConfig.DEBUG)  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) else HttpLoggingInterceptor()))
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.MINUTES).build()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(RetrofitInterface.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpBuilder)
        .build()

    val retrofitInstance: RetrofitInterface
        get() = retrofit.create<RetrofitInterface>(RetrofitInterface::class.java)
}