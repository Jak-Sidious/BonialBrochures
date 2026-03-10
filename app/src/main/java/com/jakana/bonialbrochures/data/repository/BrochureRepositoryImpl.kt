package com.jakana.bonialbrochures.data.repository

import com.jakana.bonialbrochures.data.remote.ShelfApiService
import com.jakana.bonialbrochures.domain.model.Brochure
import com.jakana.bonialbrochures.domain.repository.BrochureRepository
import javax.inject.Inject

class BrochureRepositoryImpl @Inject constructor(
    private val api: ShelfApiService
) : BrochureRepository {
    override suspend fun getBrochures(): List<Brochure> {
        return api.getShelf()
            .embedded
            .contents
            .filter { it.contentType in listOf("brochure", "brochurePremium") }
            .mapNotNull { item ->
                try {
                    item.content?.let {
                        Brochure(
                            id = it.id,
                            retailerName = it.publisher.name,
                            imageUrl = it.brochureImage,
                            contentType = item.contentType,
                            distance = it.distance
                        )
                    }
                } catch (e: Exception) { null }
            }

    }
}