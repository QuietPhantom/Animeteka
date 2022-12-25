package com.example.animeteka.data.retrofit.common

import com.example.animeteka.data.retrofit.Interface.RetrofitServices
import com.example.animeteka.data.retrofit.`object`.RetrofitClient

object Common {
    private val BASE_URL = "https://kitsu.io"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}