package com.example.animeteka.data.retrofit

import com.example.animeteka.data.retrofit.entities.RetrofitApiCallbackEntities
import com.example.animeteka.data.retrofit.entities.RetrofitApiCallbackEntity
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @GET("/api/edge/anime")
    fun getAnimeTitlesList(@Query("page[offset]") offset: String, @Query("page[limit]") limit: String): Call<RetrofitApiCallbackEntities>

    @GET("/api/edge/anime/{titleId}")
    fun getAnimeTitleById(@Path("titleId") parameters: String): Call<RetrofitApiCallbackEntity>

    @GET("/api/edge/anime?")
    fun getAnimeTitlesListByKeyWords(@Query("filter[text]") parameters: String): Call<RetrofitApiCallbackEntities>
}