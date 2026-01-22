package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DottedContainer(
    modifier: Modifier = Modifier,
    dotRadius: Dp = 2.dp,
    dotSpacing: Dp = 20.dp,
    dotColor: Color = Color.Gray.copy(alpha = .75f),
    content: @Composable BoxScope.() -> Unit,
) {
  Box(
      modifier =
          modifier.drawWithContent {
            val radiusPx = dotRadius.toPx()
            val spacingPx = dotSpacing.toPx()

            val cols = ((size.width - radiusPx * 2) / spacingPx).toInt() + 1
            val rows = ((size.height - radiusPx * 2) / spacingPx).toInt() + 1

            val totalDotsWidth = (cols - 1) * spacingPx
            val totalDotsHeight = (rows - 1) * spacingPx

            val startX = (size.width - totalDotsWidth) / 2f
            val startY = (size.height - totalDotsHeight) / 2f

            var y = startY
            repeat(rows) {
              var x = startX
              repeat(cols) {
                drawCircle(color = dotColor, radius = radiusPx, center = Offset(x, y))
                x += spacingPx
              }
              y += spacingPx
            }
            drawContent()
          },
      content = content,
  )
}
