package com.example.animeteka.data.repositories

import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.businesslogic.iRepositories.ITitleRepository
import com.example.animeteka.data.database.DAO

class TitleRepository(private val DAO: DAO) : ITitleRepository {

    override suspend fun getAllTitles(): List<TitleEntity> {
        return DAO.getAllTitles().map {it.toTitleEntity()}
    }

    override suspend fun getTitleById(id: Int): TitleEntity {
        return DAO.getTitleById(id).toTitleEntity()
    }

    override suspend fun getCountTitleById(id: Int): Int {
        return DAO.getCountTitleById(id)
    }

    override suspend fun saveTitle(title: TitleEntity) {
        DAO.saveTitle(title.toTitle())
    }

    override suspend fun deleteTitle(title: TitleEntity) {
        DAO.deleteTitle(title.toTitle())
    }
}