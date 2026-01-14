package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.Transform
import com.ruhaan.orangeeditor.domain.model.layer.isIntersect

@Composable
fun GestureBox(
    width: Dp,
    height: Dp,
    state: EditorState,
    onLayerTapped: (String) -> Unit,
    onUpdateLayer: (Layer) -> Unit,
) {
  val selectedLayerId = state.selectedLayerId
  val selectedLayer = state.layers.firstOrNull { it.id == selectedLayerId } ?: return

  val currentLayer by rememberUpdatedState(selectedLayer)

  Box(
      modifier =
          Modifier.border(2.dp, Color.Red)
              .size(width, height)
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
                detectTransformGestures { _, pan, zoom, rotation ->
                  val newX = currentLayer.transform.x + pan.x
                  val newY = currentLayer.transform.y + pan.y

                  val updated =
                      when (val layer = currentLayer) {
                        is ImageLayer -> {
                          val newScale = (layer.transform.scale * zoom).coerceIn(0.1f, 2f)
                          val t =
                              Transform(
                                  x = newX,
                                  y = newY,
                                  scale = newScale,
                                  rotation = layer.transform.rotation + rotation,
                              )
                          layer.copy(transform = t)
                        }

                        is TextLayer -> {
                          val newFontSizeInPx = (layer.fontSizeInPx * zoom).coerceIn(0f, 300f)
                          val t =
                              Transform(
                                  x = newX,
                                  y = newY,
                                  scale = currentLayer.transform.scale,
                                  rotation = layer.transform.rotation + rotation,
                              )
                          layer.copy(
                              transform = t,
                              fontSizeInPx = newFontSizeInPx.toInt(),
                          )
                        }
                      }

                  onUpdateLayer(updated)
                }
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
        .firstOrNull { layer ->
          when (layer) {
            is TextLayer -> layer.isIntersect(tapX, tapY)
            is ImageLayer -> layer.isIntersect(tapX, tapY)
          }
        }
