package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.util.EditorRenderer
import kotlin.math.min

@Composable
fun EditorCanvas(
    modifier: Modifier = Modifier,
    canvasFormat: CanvasFormat,
    state: EditorState,
    onCanvasSize: (IntSize) -> Unit,
) {

  val renderer = remember { EditorRenderer() }

  BoxWithConstraints(
      modifier = modifier.fillMaxSize().padding(16.dp),
      contentAlignment = Alignment.Center,
  ) {
    // Calculate aspect ratio from format's pixel dimensions
    val aspectRatio = canvasFormat.width.toFloat() / canvasFormat.height.toFloat()

    // Available space from parent
    val maxWidth = maxWidth
    val maxHeight = maxHeight

    // Calculate canvas size that fits within constraints while maintaining aspect ratio
    val (canvasWidth, canvasHeight) =
        if (aspectRatio >= 1f) {
          // Landscape: Width is limiting factor
          val width = min(maxWidth.value, maxHeight.value * aspectRatio).dp
          val height = width / aspectRatio
          width to height
        } else {
          // Portrait: Height is limiting factor
          val height = min(maxHeight.value, maxWidth.value / aspectRatio).dp
          val width = height * aspectRatio
          width to height
        }

    Box(
        modifier =
            Modifier.border(width = 2.dp, color = Color.Gray.copy(alpha = 0.4f))
                .size(width = canvasWidth, height = canvasHeight)
                .onSizeChanged { onCanvasSize(it) },
        contentAlignment = Alignment.Center,
    ) {
      Surface(shadowElevation = 2.dp, color = Color.White) {
        Canvas(modifier = Modifier.size(width = canvasWidth, height = canvasHeight)) {
          drawIntoCanvas { composeCanvas ->
            renderer.draw(composeCanvas.nativeCanvas, state.layers)
          }
        }
      }
    }
  }
}
