package com.example.animeteka.retrofit

import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntities
import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntity
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @GET("/api/edge/anime?page[limit]=10")
    fun getAnimeTitlesList(@Query("page[offset]") parameters: String): Call<RetrofitApiCallbackEntities>

    @GET("/api/edge/anime/{titleId}")
    fun getAnimeTitleById(@Path("titleId") parameters: String): Call<RetrofitApiCallbackEntity>
}