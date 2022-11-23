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
    val description: String,
    val posterImage: ImageUrls,
    val averageRating: String
)

data class ImageUrls(
    val tiny: String,
    val large: String,
    val small: String,
    val medium: String,
    val original: String
)