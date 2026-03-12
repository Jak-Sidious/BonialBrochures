package com.jakana.bonialbrochures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jakana.bonialbrochures.presentation.BrochureViewModel
import com.jakana.bonialbrochures.presentation.theme.BonialBrochuresTheme
import com.jakana.bonialbrochures.presentation.ui.BrochureScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: BrochureViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            BonialBrochuresTheme {
                BrochureScreen(uiState = uiState)
            }
        }
    }
}