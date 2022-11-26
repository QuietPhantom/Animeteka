package com.example.animeteka.entities

data class RetrofitApiCallbackEntity(
    val data: List<RetrofitApiDataEntity>
)

data class RetrofitApiDataEntity(
    val id: Int,
    val attributes: AttributesEntity
)

data class AttributesEntity(
    val canonicalTitle: String,
    val titles: TitleEntity,
    val description: String,
    val posterImage: ImageUrls,
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

data class ImageUrls(
    val tiny: String,
    val large: String,
    val small: String,
    val medium: String,
    val original: String
)

data class TitleEntity(
    val en: String
)