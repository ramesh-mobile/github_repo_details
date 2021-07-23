package com.avatar.inpsection.data.network

import com.example.githubrepodetailsapp.data.model.responses.RepoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap


/**
 * Created by ramesh on 19-07-2021
 */
interface RetrofitInterface {

    companion object {
        const val BASE_URL = "https://api.github.com"
    }

    @GET("search/repositories")
    suspend fun getRepository(@QueryMap filter: Map<String?, String?>?): Response<RepoModel>?

    /*suspend fun getRepository(@Query("q") q: String,
                          @Query("per_page") perPage: Int,
                          @Query("page") page: Int,
                          @Query("order") order: String,
                          @Query("sort") sort: String
    ): Response<RepoModel>?*/


}