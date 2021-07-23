package com.example.githubrepodetailsapp.data.network

import com.avatar.inpsection.data.network.RetrofitInterface
import com.avatar.inpsection.data.network.RetrofitService
import com.example.githubrepodetailsapp.data.model.responses.RepoModel
import retrofit2.Response

/**
 * Created by ramesh on 20-07-2021
 */
object NetworkSinglton {

    private var mInstance: NetworkSinglton? = null
    val instance: NetworkSinglton
        get() = if (mInstance == null) NetworkSinglton.also {
            mInstance = it
        } else mInstance!!

    suspend fun getRepos(map: Map<String?, String?>?): Response<RepoModel>? {
        val api: RetrofitInterface = RetrofitService.retrofitInstance
        var response = api.getRepository(map)
        return response
    }
}
