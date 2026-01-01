package com.ruhaan.orangeeditor.presentation.editor

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.ruhaan.orangeeditor.domain.model.layer.Adjustments
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.ImageFilter
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.NeutralAdjustments
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.Transform
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditorViewModel : ViewModel() {

  private val _state = MutableStateFlow(EditorState())
  val state = _state.asStateFlow()

  fun resetState() {
    _state.update { it.copy(layers = emptyList(), selectedLayerId = null) }
  }

  fun addLayer(layer: Layer) {
    _state.update { it.copy(layers = it.layers + layer, selectedLayerId = layer.id) }
  }

  fun addImageLayer(
      bitmap: Bitmap,
      imageFilter: ImageFilter = ImageFilter.NO_FILTER,
      canvasWidthInPx: Float,
      canvasHeightInPx: Float,
  ) {
    val scale =
        minOf(
            canvasWidthInPx / bitmap.width,
            canvasHeightInPx / bitmap.height,
            1f,
        ) * 0.9f

    // Why? because we want to place image at center of canvas when user import images.
    val x = canvasWidthInPx / 2f
    val y = canvasHeightInPx / 2f

    val layer =
        ImageLayer(
            id = UUID.randomUUID().toString(),
            bitmap = bitmap,
            imageFilter = imageFilter,
            adjustments = NeutralAdjustments,
            transform = Transform(x = x, y = y, scale = scale, rotation = 0f),
            zIndex = (_state.value.layers.maxOfOrNull { it.zIndex } ?: 0) + 1,
            originalWidth = bitmap.width,
            originalHeight = bitmap.height,
        )

    addLayer(layer = layer)
  }

  fun addTextLayer(
      text: String,
      color: Color,
      fontSizeInPx: Int,
      fontWeight: FontWeight,
      fontStyle: FontStyle,
      canvasWidthInPx: Float,
      canvasHeightInPx: Float,
  ) {
    val x = canvasWidthInPx / 2f
    val y = canvasHeightInPx / 2f

    val layer =
        TextLayer(
            id = UUID.randomUUID().toString(),
            text = text,
            color = color,
            fontSizeInPx = fontSizeInPx,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            transform =
                Transform(
                    x = x,
                    y = y,
                    scale = 1f,
                    rotation = 0f,
                ),
            zIndex = (_state.value.layers.maxOfOrNull { it.zIndex } ?: 0) + 1,
            visible = true,
        )

    addLayer(layer)
  }

  fun selectLayer(id: String?) {
    _state.update { state -> state.copy(selectedLayerId = id) }
  }

  fun updateLayer(updatedLayer: Layer) {
    _state.update { state ->
      state.copy(
          layers =
              state.layers.map { layer -> if (layer.id == updatedLayer.id) updatedLayer else layer }
      )
    }
  }

  fun removeLayer(id: String) {
    _state.update { state -> state.copy(layers = state.layers.filterNot { it.id == id }) }
  }

  fun getSelectedLayer(): Layer? {
    return _state.value.layers.firstOrNull { it.id == _state.value.selectedLayerId }
  }

  fun getSelectedImagerLayer(): ImageLayer? {
    val currentSelectedLayer = getSelectedLayer()
    if (currentSelectedLayer is ImageLayer) return currentSelectedLayer
    return null
  }

  fun updateBitmapOfSelectedImageLayer(updatedBitmap: Bitmap) {
    val selectedLayer = getSelectedLayer()
    val selectedImageLayer = selectedLayer as? ImageLayer
    selectedImageLayer?.let { updateLayer(updatedLayer = it.copy(bitmap = updatedBitmap)) }
  }

  fun updateImageFilterOfSelectedImagerLayer(imageFilter: ImageFilter) {
    val selectedLayer = getSelectedLayer()
    val selectedImageLayer = selectedLayer as? ImageLayer
    selectedImageLayer?.let { updateLayer(updatedLayer = it.copy(imageFilter = imageFilter)) }
  }

  fun updateAdjustmentsOfSelectedImageLayer(adjustments: Adjustments) {
    val selectedLayer = getSelectedLayer()
    val selectedImageLayer = selectedLayer as? ImageLayer
    selectedImageLayer?.let { updateLayer(updatedLayer = it.copy(adjustments = adjustments)) }
  }
}
