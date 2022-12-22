package com.example.animeteka.presentation.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animeteka.R
import com.example.animeteka.retrofit.RetrofitServices
import com.example.animeteka.retrofit.common.Common
import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntities
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlideshowViewModel : ViewModel() {
    private lateinit var retrofitService: RetrofitServices

    val livedata = MutableLiveData<RetrofitApiCallbackEntities>()

    fun initApi(){
        retrofitService = Common.retrofitService
    }

    fun getNewAnimeTitlesListByKeyWords(keyWords: String, context: Context) {
        var dialog: AlertDialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        dialog.show()
        retrofitService.getAnimeTitlesListByKeyWords(keyWords).enqueue(object:
            Callback<RetrofitApiCallbackEntities> {

            override fun onFailure(call: Call<RetrofitApiCallbackEntities>, t: Throwable) {
                t.printStackTrace()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<RetrofitApiCallbackEntities>, response: Response<RetrofitApiCallbackEntities>) {
                livedata.postValue(response.body()!!)
                if (response.body()!!.data.isEmpty()) Toast.makeText(context, context.resources.getString(R.string.search_null), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
    }
}