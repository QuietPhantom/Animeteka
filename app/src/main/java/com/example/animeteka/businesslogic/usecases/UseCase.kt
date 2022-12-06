package com.example.animeteka.businesslogic.usecases

import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.businesslogic.iRepositories.ITitleRepository

class UseCase (private val titleRepository: ITitleRepository) : IUseCase {
    override suspend fun getAllTitles(): List<TitleEntity>
            = titleRepository.getAllTitles()

    override suspend fun getTitleById(id: Int): TitleEntity
            = titleRepository.getTitleById(id)

    override suspend fun saveTitle(title: TitleEntity)
            = titleRepository.saveTitle(title)

    override suspend fun deleteTitle(title: TitleEntity)
            = titleRepository.deleteTitle(title)
}