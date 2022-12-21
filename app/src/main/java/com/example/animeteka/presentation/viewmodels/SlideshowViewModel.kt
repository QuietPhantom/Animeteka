package com.example.animeteka.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animeteka.retrofit.RetrofitServices
import com.example.animeteka.retrofit.common.Common
import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntities
import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlideshowViewModel : ViewModel() {
    private lateinit var retrofitService: RetrofitServices

    val livedata = MutableLiveData<RetrofitApiCallbackEntities>()

    fun initApi(){
        retrofitService = Common.retrofitService
    }

    fun getNewAnimeTitlesListByKeyWords(keyWords: String) {
        retrofitService.getAnimeTitlesListByKeyWords(keyWords).enqueue(object:
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