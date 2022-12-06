package com.example.animeteka.businesslogic.iRepositories

import com.example.animeteka.businesslogic.entities.TitleEntity

interface ITitleRepository {
    suspend fun getAllTitles() : List<TitleEntity>
    suspend fun getTitleById(id: Int) : TitleEntity
    suspend fun saveTitle(title: TitleEntity)
    suspend fun deleteTitle(title: TitleEntity)
}