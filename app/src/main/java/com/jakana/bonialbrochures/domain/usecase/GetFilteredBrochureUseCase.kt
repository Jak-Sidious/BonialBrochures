package com.jakana.bonialbrochures.domain.usecase

import com.jakana.bonialbrochures.domain.model.Brochure
import com.jakana.bonialbrochures.domain.repository.BrochureRepository
import javax.inject.Inject

private val VALID_TYPES = setOf("brochure", "brochurePremium")
private const val MAX_KM = 5.0

class GetFilteredBrochuresUseCase @Inject constructor(
    private val repository: BrochureRepository
) {
    suspend operator fun invoke(): List<Brochure> {
        return repository.getBrochures()
            .filter { it.contentType in VALID_TYPES }
            .filter { it.distance == null || it.distance < MAX_KM }
    }
}