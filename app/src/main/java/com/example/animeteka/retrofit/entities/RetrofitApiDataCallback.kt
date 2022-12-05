package com.example.animeteka.retrofit.entities

data class RetrofitApiCallbackEntities(
    val data: List<RetrofitApiDataEntity>
)

data class RetrofitApiCallbackEntity(
    val data: RetrofitApiDataEntity
)

data class RetrofitApiDataEntity(
    val id: Int,
    val attributes: AttributesEntity
)

data class AttributesEntity(
    val canonicalTitle: String,
    val titles: TitlesEntity,
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

data class TitlesEntity(
    val en: String
)