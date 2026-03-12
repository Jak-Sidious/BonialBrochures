package com.jakana.bonialbrochures.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShelfResponse(
    @Json(name = "_embedded")
    val embedded: Embedded
)

@JsonClass(generateAdapter = true)
data class Embedded(
    val contents: List<ContentItem>
)

@JsonClass(generateAdapter = true)
data class ContentItem(
    val contentType: String,
    val content: BrochureContent? = null // nullable — array types return null safely
)

@JsonClass(generateAdapter = true)
data class BrochureContent(
    val id: Long,
    val brochureImage: String?, // nullable — show placeholder when null
    val distance: Double?, // nullable — include item when null
    val publisher: Publisher
)

@JsonClass(generateAdapter = true)
data class Publisher(val id: String, val name: String)