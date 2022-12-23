package com.example.animeteka.data.retrofit.common

import com.example.animeteka.data.retrofit.RetrofitServices
import com.example.animeteka.data.retrofit.RetrofitClient

object Common {
    private val BASE_URL = "https://kitsu.io"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}