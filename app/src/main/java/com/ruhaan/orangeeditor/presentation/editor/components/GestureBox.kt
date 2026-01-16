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
          when (val layer = currentLayer) {
            is TextLayer -> {
              val w = layer.textWidthPx.toFloat()
              val h = layer.textHeightPx.toFloat()

              LayerBounds(
                centerX = layer.transform.x + w / 2f,
                centerY = layer.transform.y + h / 2f,
                width = w,
                height = h,
              )
            }

            is ImageLayer -> {
              val bitmap = layer.bitmap ?: return@derivedStateOf null
              val w = bitmap.width * layer.transform.scale
              val h = bitmap.height * layer.transform.scale
              LayerBounds(
                  centerX = layer.transform.x,
                  centerY = layer.transform.y,
                  width = w,
                  height = h,
              )
            }
          }
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

                      val updated =
                          when (val layer = currentLayer) {
                            is ImageLayer -> {
                              val newScale = (layer.transform.scale * zoom).coerceIn(0.1f, 2f)
                              val t =
                                  Transform(
                                      x = snapX,
                                      y = snapY,
                                      scale = newScale,
                                      rotation = layer.transform.rotation + rotation,
                                  )
                              layer.copy(transform = t)
                            }

                            is TextLayer -> {
                              val newFontSizeInPx = (layer.fontSizeInPx * zoom).coerceIn(0f, 300f)
                              val t =
                                  Transform(
                                      x = snapX,
                                      y = snapY,
                                      scale = 1f,
                                      rotation = layer.transform.rotation + rotation,
                                  )
                              layer.copy(
                                  transform = t,
                                  fontSizeInPx = newFontSizeInPx.toInt(),
                              )
                            }
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
        .firstOrNull { layer ->
          when (layer) {
            is TextLayer -> layer.isIntersect(tapX, tapY)
            is ImageLayer -> layer.isIntersect(tapX, tapY)
          }
        }

// Original code

// @Composable
// fun GestureBox(
//    width: Dp,
//    height: Dp,
//    state: EditorState,
//    onLayerTapped: (String) -> Unit,
//    onUpdateLayer: (Layer) -> Unit,
// ) {
//  val selectedLayerId = state.selectedLayerId
//  val selectedLayer = state.layers.firstOrNull { it.id == selectedLayerId } ?: return
//
//  val currentLayer by rememberUpdatedState(selectedLayer)
//
//  Box(
//      modifier =
//          Modifier.size(width, height)
//              .pointerInput(state.layers) {
//                detectTapGestures { offset ->
//                  val tappedText =
//                      detectTappedLayer(
//                          layers = state.layers,
//                          tapX = offset.x,
//                          tapY = offset.y,
//                      )
//
//                  tappedText?.let { onLayerTapped(it.id) }
//                }
//              }
//              .pointerInput(Unit) {
//                detectTransformGestures { _, pan, zoom, rotation ->
//                  val newX = currentLayer.transform.x + pan.x
//                  val newY = currentLayer.transform.y + pan.y
//
//                  val updated =
//                      when (val layer = currentLayer) {
//                        is ImageLayer -> {
//                          val newScale = (layer.transform.scale * zoom).coerceIn(0.1f, 2f)
//                          val t =
//                              Transform(
//                                  x = newX,
//                                  y = newY,
//                                  scale = newScale,
//                                  rotation = layer.transform.rotation + rotation,
//                              )
//                          layer.copy(transform = t)
//                        }
//
//                        is TextLayer -> {
//                          val newFontSizeInPx = (layer.fontSizeInPx * zoom).coerceIn(0f, 300f)
//                          val t =
//                              Transform(
//                                  x = newX,
//                                  y = newY,
//                                  scale = currentLayer.transform.scale,
//                                  rotation = layer.transform.rotation + rotation,
//                              )
//                          layer.copy(
//                              transform = t,
//                              fontSizeInPx = newFontSizeInPx.toInt(),
//                          )
//                        }
//                      }
//
//                  onUpdateLayer(updated)
//                }
//              }
//  )
// }
