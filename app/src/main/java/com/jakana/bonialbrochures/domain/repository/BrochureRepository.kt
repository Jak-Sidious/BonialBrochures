package com.jakana.bonialbrochures.domain.repository

import com.jakana.bonialbrochures.domain.model.Brochure

interface BrochureRepository {
    suspend fun getBrochures(): List<Brochure>
}