package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.EmojiLayer
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.Transform

@Composable
fun SelectedLayerGestureLayer(
    state: EditorState,
    onUpdateLayer: (Layer) -> Unit,
) {
  val selectedLayer = state.layers.firstOrNull { it.id == state.selectedLayerId } ?: return

  val currentLayer by rememberUpdatedState(selectedLayer)

  Box(
      modifier =
          Modifier.fillMaxSize().pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, rotation ->
              val newScale = (currentLayer.transform.scale * zoom).coerceIn(0.1f, 2f)

              val newX = currentLayer.transform.x + pan.x
              val newY = currentLayer.transform.y + pan.y

              val updated =
                  when (val layer = currentLayer) {
                    is ImageLayer -> {
                      val t =
                          Transform(
                              x = newX,
                              y = newY,
                              scale = newScale,
                              rotation = layer.transform.rotation + rotation,
                          )
                      layer.copy(transform = t)
                    }

                    is EmojiLayer -> {
                      val size = layer.bitmap.width.toFloat()
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
                      val t =
                          Transform(
                              x = newX,
                              y = newY,
                              scale = newScale,
                              rotation = layer.transform.rotation + rotation,
                          )
                      layer.copy(transform = t)
                    }
                  }

              onUpdateLayer(updated)
            }
          }
  )
}
