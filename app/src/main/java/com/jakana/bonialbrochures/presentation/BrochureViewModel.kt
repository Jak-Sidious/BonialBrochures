package com.jakana.bonialbrochures.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakana.bonialbrochures.domain.usecase.GetFilteredBrochuresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrochureViewModel @Inject constructor(
    private val getFilteredBrochures: GetFilteredBrochuresUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<BrochureUiState>(BrochureUiState.Loading)
    val uiState: StateFlow<BrochureUiState> = _uiState.asStateFlow()

    init { loadBrochures() }

    fun loadBrochures() {
        viewModelScope.launch {
            _uiState.value = BrochureUiState.Loading
            try {
                val brochures = getFilteredBrochures()
                _uiState.value = BrochureUiState.Success(brochures)
            } catch (e: Exception) {
                _uiState.value = BrochureUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}