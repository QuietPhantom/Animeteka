package com.example.animeteka.data

import android.app.Application
import androidx.room.Room
import com.example.animeteka.businesslogic.usecases.UseCase
import com.example.animeteka.businesslogic.usecases.IUseCase
import com.example.animeteka.data.database.TitleDatabase
import com.example.animeteka.data.repositories.TitleRepository

class NoteApplication : Application() {

    private lateinit var database : TitleDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            TitleDatabase::class.java,
            "anime-database"
        ).build()
    }

    private var noteUseCase : IUseCase? = null

    fun getUseCase() : IUseCase {
        if (noteUseCase == null){
            noteUseCase = UseCase(TitleRepository(database.getDAO()))
        }
        return noteUseCase!!
    }

}