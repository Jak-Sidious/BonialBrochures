package com.jakana.bonialbrochures.domain.model

data class Brochure(
    val id: Long,
    val retailerName: String,
    val imageUrl: String?,
    val contentType: String,
    val distance: Double?
) {
    val isPremium: Boolean get() = contentType == "brochurePremium"
}
