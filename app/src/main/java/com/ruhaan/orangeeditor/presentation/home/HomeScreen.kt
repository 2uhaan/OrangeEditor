package com.ruhaan.orangeeditor.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.CanvasFormat
import com.ruhaan.orangeeditor.presentation.home.components.CanvasGrid
import com.ruhaan.orangeeditor.presentation.home.components.HomeHeader
import com.ruhaan.orangeeditor.presentation.theme.BackgroundLight


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFormatSelected: (CanvasFormat) -> Unit,  // Navigation callback
) {
    // Column stacks header and grid vertically
    Column(
        modifier = modifier
            .fillMaxSize()  // Take full screen
            .background(BackgroundLight)  // Light gray background
            .padding(top = 60.dp)  // Top padding for status bar area
    ) {
        // Grid of canvas format cards
        CanvasGrid(
            headerContent = { HomeHeader() },  // Pass header as lambda
            canvasFormats = CanvasFormat.allFormats,
            onFormatClick = onFormatSelected,  // ✨ NEW: Pass callback down to grid
        )
    }
}
