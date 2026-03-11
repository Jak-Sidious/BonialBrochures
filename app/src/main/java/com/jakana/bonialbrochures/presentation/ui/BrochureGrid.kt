package com.jakana.bonialbrochures.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.jakana.bonialbrochures.domain.model.Brochure


@Composable
fun BrochureGrid(brochures: List<Brochure>) {
    val orientation = LocalConfiguration.current.orientation
    val columns = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = brochures,
            key = { it.id }, // Performance: stable keys
            span = { brochure ->
                if (brochure.isPremium) GridItemSpan(maxLineSpan) // full width
                else GridItemSpan(1)
            }
        ) { brochure ->
            BrochureCard(brochure = brochure)
        }

    }
}