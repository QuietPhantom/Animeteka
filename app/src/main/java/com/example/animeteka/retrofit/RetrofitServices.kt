package com.example.animeteka.retrofit

import com.example.animeteka.entities.RetrofitApiCallbackEntity
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @GET("/api/edge/anime??page[limit]=10")
    fun getAnimeTitlesList(@Query("page[offset]") parameters: String): Call<RetrofitApiCallbackEntity>
}