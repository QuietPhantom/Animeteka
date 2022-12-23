package com.example.animeteka.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.businesslogic.usecases.IUseCase
import com.example.animeteka.data.Application

class GalleryViewModel : ViewModel() {

    private lateinit var UseCase : IUseCase

    fun init(application: Application){
        UseCase = application.getUseCase()
    }

    fun getTitles(): LiveData<List<TitleEntity>> = liveData {
            emit(UseCase.getAllTitles())
    }

    suspend fun saveTitle(title: TitleEntity){
        UseCase.saveTitle(title)
    }

    suspend fun deleteTitle(title: TitleEntity){
        UseCase.deleteTitle(title)
    }
}