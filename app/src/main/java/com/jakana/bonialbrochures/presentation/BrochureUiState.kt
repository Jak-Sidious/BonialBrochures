package com.jakana.bonialbrochures.presentation

import com.jakana.bonialbrochures.domain.model.Brochure

sealed class BrochureUiState {
    object Loading : BrochureUiState()
    data class Success(val brochures: List<Brochure>) : BrochureUiState()
    data class Error(val message: String) : BrochureUiState()
}