package com.example.animeteka.common

import com.example.animeteka.retrofit.RetrofitServices
import com.example.animeteka.retrofit.RetrofitClient

object Common {
    private val BASE_URL = "https://kitsu.io"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}