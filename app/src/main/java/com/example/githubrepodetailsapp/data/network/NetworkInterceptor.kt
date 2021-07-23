package com.avatar.inpsection.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.githubrepodetailsapp.GithubProjectApplication
import com.example.githubrepodetailsapp.data.network.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by ramesh on 21-07-2021
 */
class NetworkInterceptor : Interceptor{

    var applicationContext: Context? = GithubProjectApplication.baseContext

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NoInternetException("No Internet Connection")
        }
        return chain.proceed(chain.request())
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext?.
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}