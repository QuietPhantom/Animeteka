package com.example.animeteka.presentation.viewmodels

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.businesslogic.usecases.IUseCase
import com.example.animeteka.data.Application
import com.example.animeteka.retrofit.common.Common
import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntity
import com.example.animeteka.retrofit.RetrofitServices
import dmax.dialog.SpotsDialog
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
        var dialog: AlertDialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        dialog.show()
        retrofitService.getAnimeTitleById(titleId.toString()).enqueue(object:
            Callback<RetrofitApiCallbackEntity> {
            override fun onFailure(call: Call<RetrofitApiCallbackEntity>, t: Throwable) {
                t.printStackTrace()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<RetrofitApiCallbackEntity>, response: Response<RetrofitApiCallbackEntity>) {
                livedata.postValue(response.body()!!)
                dialog.dismiss()
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