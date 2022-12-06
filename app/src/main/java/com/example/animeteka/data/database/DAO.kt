package com.example.animeteka.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.animeteka.data.entities.Title

@Dao
interface DAO {
    @Query("Select * From title")
    suspend fun getAllTitles() : List<Title>

    @Query("Select * From title Where id = :id")
    suspend fun getTitleById(id: Int) : Title

    @Insert
    suspend fun saveTitle(title: Title)

    @Delete
    suspend fun deleteTitle(title: Title)
}