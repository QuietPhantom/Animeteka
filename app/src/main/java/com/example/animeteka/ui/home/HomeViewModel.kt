package com.example.animeteka.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animeteka.common.Common
import com.example.animeteka.entities.RetrofitApiCallbackEntity
import com.example.animeteka.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private lateinit var retrofitService: RetrofitServices

    val livedata = MutableLiveData<RetrofitApiCallbackEntity>()

    fun initApi(){
        retrofitService = Common.retrofitService
    }

    fun getNewAnimeTitlesList() {
        retrofitService.getAnimeTitlesList((0..10000).random().toString()).enqueue(object:
            Callback<RetrofitApiCallbackEntity> {

            override fun onFailure(call: Call<RetrofitApiCallbackEntity>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<RetrofitApiCallbackEntity>, response: Response<RetrofitApiCallbackEntity>) {
                livedata.postValue(response.body()!!)
            }
        })
    }
}