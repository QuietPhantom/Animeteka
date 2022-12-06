package com.example.animeteka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.animeteka.businesslogic.entities.TitleEntity

@Entity
data class Title(
    @PrimaryKey val id: Int,
    val canonicalTitle: String,
    val enTitle: String,
    val description: String,
    val posterImage: String,
    val averageRating: String,
    val userCount: String,
    val startDate: String,
    val endDate: String,
    val ageRating: String,
    val ageRatingGuide: String,
    val subtype: String,
    val status: String,
    val episodeCount: String,
    val episodeLength: String,
    val totalLength: String
)
{
    fun toTitleEntity() = TitleEntity(id, canonicalTitle, enTitle, description, posterImage, averageRating, userCount, startDate, endDate, ageRating, ageRatingGuide, subtype, status, episodeCount, episodeLength, totalLength)
}
