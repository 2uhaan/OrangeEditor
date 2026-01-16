package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import com.ruhaan.orangeeditor.domain.model.format.AlignmentConstants
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.LayerBounds
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.Transform
import com.ruhaan.orangeeditor.domain.model.layer.isIntersect
import com.ruhaan.orangeeditor.util.snapToGuides

@Composable
fun GestureBox(
  width: Dp,
  height: Dp,
  state: EditorState,
  onLayerTapped: (String) -> Unit,
  onUpdateLayer: (Layer) -> Unit,
  onDragStateChange: (Boolean) -> Unit,
  onLayerBoundsChange: (LayerBounds?) -> Unit,
  canvasWidthPx: Float,
  canvasHeightPx: Float,
) {
  val selectedLayerId = state.selectedLayerId
  val selectedLayer = state.layers.firstOrNull { it.id == selectedLayerId } ?: return

  val currentLayer by rememberUpdatedState(selectedLayer)

  // Calculate layer bounds (in px) from currentLayer
  val layerBounds by
  remember(currentLayer) {
    derivedStateOf {
      val bitmap = currentLayer.bitmap ?: return@derivedStateOf null
      val w = bitmap.width * currentLayer.transform.scale
      val h = bitmap.height * currentLayer.transform.scale
      LayerBounds(
        centerX = currentLayer.transform.x,
        centerY = currentLayer.transform.y,
        width = w,
        height = h,
      )
    }
  }

  // Notify parent about bounds
  LaunchedEffect(layerBounds) { onLayerBoundsChange(layerBounds) }

  Box(
    modifier =
      Modifier.size(width, height)
        .pointerInput(state.layers) {
          detectTapGestures { offset ->
            val tappedText =
              detectTappedLayer(
                layers = state.layers,
                tapX = offset.x,
                tapY = offset.y,
              )
            tappedText?.let { onLayerTapped(it.id) }
          }
        }
        .pointerInput(Unit) {
          detectTransformGesturesWithEnd(
            onGestureStart = { onDragStateChange(true) },
            onGesture = { _, pan, zoom, rotation ->
              val newX = currentLayer.transform.x + pan.x
              val newY = currentLayer.transform.y + pan.y
              val newScale = (currentLayer.transform.scale * zoom).coerceIn(0.1f, 2f)

              val snapX =
                snapToGuides(
                  value = newX,
                  canvasWidth = canvasWidthPx,
                  canvasHeight = canvasHeightPx,
                  layerWidth = layerBounds?.width ?: 0f,
                  layerHeight = layerBounds?.height ?: 0f,
                  threshold = AlignmentConstants.ALIGNMENT_THRESHOLD_PX,
                )
              val snapY =
                snapToGuides(
                  value = newY,
                  canvasWidth = canvasWidthPx,
                  canvasHeight = canvasHeightPx,
                  layerWidth = layerBounds?.width ?: 0f,
                  layerHeight = layerBounds?.height ?: 0f,
                  threshold = AlignmentConstants.ALIGNMENT_THRESHOLD_PX,
                )

              val newTransform =
                Transform(
                  x = snapX,
                  y = snapY,
                  scale = newScale,
                  rotation = currentLayer.transform.rotation + rotation,
                )

              val updated =
                when (val layer = currentLayer) {
                  is ImageLayer -> layer.copy(transform = newTransform)
                  is TextLayer -> layer.copy(transform = newTransform)
                }

              onUpdateLayer(updated)
            },
            onGestureEnd = {
              onDragStateChange(false) // NOW THIS GETS CALLED
            },
          )
        }
  )
}

fun detectTappedLayer(
  layers: List<Layer>,
  tapX: Float,
  tapY: Float,
): Layer? =
  layers
    .asSequence()
    .filter { it.visible }
    .sortedByDescending { it.zIndex } // top-most first
    .firstOrNull { layer -> layer.isIntersect(tapX = tapX, tapY = tapY) }
