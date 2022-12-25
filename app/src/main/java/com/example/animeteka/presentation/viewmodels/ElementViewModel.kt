package com.example.animeteka.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.businesslogic.usecases.IUseCase
import com.example.animeteka.data.Application
import com.example.animeteka.data.retrofit.common.Common
import com.example.animeteka.data.retrofit.model.RetrofitApiCallbackEntity
import com.example.animeteka.data.retrofit.Interface.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElementViewModel : ViewModel() {
    private lateinit var retrofitService: RetrofitServices
    private lateinit var UseCase : IUseCase

    val livedata = MutableLiveData<RetrofitApiCallbackEntity>()

    fun initApi(application: Application){
        retrofitService = Common.retrofitService
        UseCase = application.getUseCase()
    }

    fun getAnimeTitleById(titleId: Int, context: Context) {
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

    suspend fun saveTitle(title: TitleEntity){
        UseCase.saveTitle(title)
    }

    suspend fun deleteTitle(title: TitleEntity){
        UseCase.deleteTitle(title)
    }

    fun getTitleById(id: Int): LiveData<TitleEntity> = liveData {
        emit(UseCase.getTitleById(id))
    }

    fun getCountTitleById(id: Int): LiveData<Int> = liveData {
        emit(UseCase.getCountTitleById(id))
    }
}