package com.example.animeteka.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animeteka.data.retrofit.common.Common
import com.example.animeteka.data.retrofit.model.RetrofitApiCallbackEntities
import com.example.animeteka.data.retrofit.Interface.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private lateinit var retrofitService: RetrofitServices

    val livedata = MutableLiveData<RetrofitApiCallbackEntities>()

    fun initApi(){
        retrofitService = Common.retrofitService
    }

    fun getNewAnimeTitlesList(randomNumber: Int, limitCount: Int) {
        retrofitService.getAnimeTitlesList(randomNumber.toString(), limitCount.toString()).enqueue(object:
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