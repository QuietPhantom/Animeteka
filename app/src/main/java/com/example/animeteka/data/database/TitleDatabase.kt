package com.example.animeteka.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.animeteka.data.entities.Title

@Database(entities = [Title::class], version = 1)
abstract class TitleDatabase : RoomDatabase() {

    abstract fun getDAO() : DAO

}