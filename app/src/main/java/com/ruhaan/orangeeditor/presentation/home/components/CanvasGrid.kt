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
    modifier: Modifier = Modifier,
    canvasFormats: List<CanvasFormat>,
    onFormatClick: (CanvasFormat) -> Unit,
    headerContent: @Composable () -> Unit = {},
) {
  LazyVerticalGrid(
      columns = GridCells.Fixed(count = 2),
      modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(vertical = 16.dp),
  ) {
    item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {
      Column {
        Spacer(modifier = Modifier.height(20.dp))
        headerContent()
      }
    }
    items(canvasFormats) { format ->
      CanvasFormatCard(canvasFormat = format, onClick = { onFormatClick(format) })
    }
  }
}
