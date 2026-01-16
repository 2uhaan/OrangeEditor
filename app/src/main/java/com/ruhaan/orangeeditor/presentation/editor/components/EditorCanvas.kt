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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.format.AlignmentConstants
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.LayerBounds
import com.ruhaan.orangeeditor.util.EditorRenderer
import kotlin.math.abs
import kotlin.math.min

@Composable
fun EditorCanvas(
    modifier: Modifier = Modifier,
    canvasFormat: CanvasFormat,
    state: EditorState,
    onCanvasSize: (IntSize) -> Unit,
    onLayerTapped: (String) -> Unit,
    onUpdateLayer: (Layer) -> Unit,
) {
  val renderer = remember { EditorRenderer() }

  var isDragging by remember { mutableStateOf(false) }
  var layerBounds by remember { mutableStateOf<LayerBounds?>(null) }

  // Clear layerBounds when dragging ends
  LaunchedEffect(isDragging) {
    if (!isDragging) {
      layerBounds = null
    }
  }
  // Clear layerBounds when selected layer is removed
  LaunchedEffect(state.selectedLayerId, state.layers) {
    val selectedLayerExists =
        state.selectedLayerId?.let { id -> state.layers.any { it.id == id } } ?: false

    if (!selectedLayerExists) {
      layerBounds = null
    }
  }

  BoxWithConstraints(
      modifier = modifier.fillMaxSize().padding(16.dp),
      contentAlignment = Alignment.Center,
  ) {
    val aspectRatio = canvasFormat.width.toFloat() / canvasFormat.height.toFloat()
    val maxWidth = maxWidth
    val maxHeight = maxHeight

    val (canvasWidth, canvasHeight) =
        if (aspectRatio >= 1f) {
          val width = min(maxWidth.value, maxHeight.value * aspectRatio).dp
          val height = width / aspectRatio
          width to height
        } else {
          val height = min(maxHeight.value, maxWidth.value / aspectRatio).dp
          val width = height * aspectRatio
          width to height
        }

    val density = LocalDensity.current
    val canvasWidthPx = with(density) { canvasWidth.toPx() }
    val canvasHeightPx = with(density) { canvasHeight.toPx() }

    Box(
        modifier =
            Modifier.border(width = 2.dp, color = Color.Gray.copy(alpha = 0.4f))
                .size(width = canvasWidth, height = canvasHeight)
                .onSizeChanged { onCanvasSize(it) },
        contentAlignment = Alignment.Center,
    ) {
      Surface(shadowElevation = 2.dp, color = Color.White) {
        Box(modifier = Modifier.size(canvasWidth, canvasHeight)) {
          // Alignment guides overlay (also draws the image/text)
          Canvas(modifier = Modifier.size(canvasWidth, canvasHeight)) {
            drawIntoCanvas {
              renderer.draw(
                  it.nativeCanvas,
                  state.layers,
                  selectedLayerId = state.selectedLayerId,
              )
            }

            val localLayerBounds = layerBounds

            if (
                state.selectedLayerId != null &&
                    isDragging &&
                    localLayerBounds != null &&
                    state.canvasSize != IntSize.Zero
            ) {
              val canvasWidthPx = state.canvasSize.width.toFloat()
              val canvasHeightPx = state.canvasSize.height.toFloat()

              val centerX = canvasWidthPx / 2f
              val centerY = canvasHeightPx / 2f

              val left = 0f
              val right = canvasWidthPx
              val top = 0f

              val layerLeft = localLayerBounds.centerX - localLayerBounds.width / 2f
              val layerRight = localLayerBounds.centerX + localLayerBounds.width / 2f
              val layerTop = localLayerBounds.centerY - localLayerBounds.height / 2f

              val threshold = AlignmentConstants.ALIGNMENT_THRESHOLD_PX
              val color = Color(AlignmentConstants.GUIDE_LINE_COLOR)
              val strokeWidth = AlignmentConstants.GUIDE_LINE_WIDTH_PX

              val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f)

              // Vertical center line
              if (abs(localLayerBounds.centerX - centerX) <= threshold) {
                drawLine(
                    color = color,
                    start = Offset(centerX, 0f),
                    end = Offset(centerX, canvasHeightPx),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect,
                )
              }

              // Horizontal center line
              if (abs(localLayerBounds.centerY - centerY) <= threshold) {
                drawLine(
                    color = color,
                    start = Offset(0f, centerY),
                    end = Offset(canvasWidthPx, centerY),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect,
                )
              }

              // Left edge line
              if (abs(layerLeft - left) <= threshold) {
                drawLine(
                    color = color,
                    start = Offset(left, 0f),
                    end = Offset(left, canvasHeightPx),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect,
                )
              }

              // Right edge line
              if (abs(layerRight - right) <= threshold) {
                drawLine(
                    color = color,
                    start = Offset(right, 0f),
                    end = Offset(right, canvasHeightPx),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect,
                )
              }

              // Top edge line
              if (abs(layerTop - top) <= threshold) {
                drawLine(
                    color = color,
                    start = Offset(0f, top),
                    end = Offset(canvasWidthPx, top),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect,
                )
              }
            }
          }

          // Gesture
          GestureBox(
              width = canvasWidth,
              height = canvasHeight,
              state = state,
              onLayerTapped = onLayerTapped,
              onUpdateLayer = onUpdateLayer,
              onDragStateChange = { isDragging = it },
              onLayerBoundsChange = { layerBounds = it },
              canvasWidthPx = canvasWidthPx,
              canvasHeightPx = canvasHeightPx,
          )
        }
      }
    }
  }
}
