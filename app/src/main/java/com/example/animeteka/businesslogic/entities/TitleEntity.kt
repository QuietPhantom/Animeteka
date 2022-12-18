package com.example.animeteka.businesslogic.entities

import com.example.animeteka.data.entities.Title

data class TitleEntity(
    val id: Int,
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
    val totalLength: String,
    val youtubeVideoId: String
)
{
    fun toTitle() = Title(id, canonicalTitle, enTitle, description, posterImage, averageRating, userCount, startDate, endDate, ageRating, ageRatingGuide, subtype, status, episodeCount, episodeLength, totalLength, youtubeVideoId)
}
