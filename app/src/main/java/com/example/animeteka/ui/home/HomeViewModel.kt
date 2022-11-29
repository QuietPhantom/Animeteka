package com.example.animeteka.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animeteka.common.Common
import com.example.animeteka.entities.RetrofitApiCallbackEntities
import com.example.animeteka.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private lateinit var retrofitService: RetrofitServices

    val livedata = MutableLiveData<RetrofitApiCallbackEntities>()

    fun initApi(){
        retrofitService = Common.retrofitService
    }

    fun getNewAnimeTitlesList() {
        retrofitService.getAnimeTitlesList((0..10000).random().toString()).enqueue(object:
            Callback<RetrofitApiCallbackEntities> {

            override fun onFailure(call: Call<RetrofitApiCallbackEntities>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<RetrofitApiCallbackEntities>, response: Response<RetrofitApiCallbackEntities>) {
                livedata.postValue(response.body()!!)
            }
        })
    }
}