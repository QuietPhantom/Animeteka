package com.example.animeteka.businesslogic.usecases

import com.example.animeteka.businesslogic.entities.TitleEntity

interface IUseCase {
    suspend fun getAllTitles(): List<TitleEntity>
    suspend fun getTitleById(id: Int) : TitleEntity
    suspend fun saveTitle(title: TitleEntity)
    suspend fun deleteTitle(title: TitleEntity)
}