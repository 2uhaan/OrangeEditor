package com.ruhaan.orangeeditor.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.CanvasFormat

@Composable
fun CanvasGrid(
    canvasFormats: List<CanvasFormat>,  // List of all canvas options
    modifier: Modifier = Modifier,
    headerContent: @Composable () -> Unit = {}  // Accept header content
) {
    // scrollable grid layout (for adding more later - if any)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),  // Side margins
        horizontalArrangement = Arrangement.spacedBy(16.dp),  // Space between columns
        verticalArrangement = Arrangement.spacedBy(16.dp),  // Space between rows
        contentPadding = PaddingValues(vertical = 16.dp)  // Top aur bottom ke padding
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                headerContent()
            }
        }
        // Loop through each canvas format and create a card
        items(canvasFormats) { format ->
            CanvasFormatCard(canvasFormat = format)
        }
    }
}
