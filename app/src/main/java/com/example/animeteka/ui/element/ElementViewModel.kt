package com.example.animeteka.ui.element

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animeteka.common.Common
import com.example.animeteka.entities.RetrofitApiCallbackEntities
import com.example.animeteka.entities.RetrofitApiCallbackEntity
import com.example.animeteka.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElementViewModel : ViewModel() {
    private lateinit var retrofitService: RetrofitServices

    val livedata = MutableLiveData<RetrofitApiCallbackEntity>()

    fun initApi(){
        retrofitService = Common.retrofitService
    }

    fun getAnimeTitleById(titleId: Int) {
        retrofitService.getAnimeTitleById(titleId.toString()).enqueue(object:
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