package com.ruhaan.orangeeditor.presentation.editor

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.domain.model.layer.Adjustment
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.ImageFilter
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.NeutralAdjustment
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.Transform
import com.ruhaan.orangeeditor.domain.repository.OrangeRepository
import com.ruhaan.orangeeditor.util.EditorRenderer
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditorViewModel @Inject constructor(private val orangeRepository: OrangeRepository) :
    ViewModel() {

  private val _editorState = MutableStateFlow(EditorState())
  val editorState = _editorState.asStateFlow()

  val allDraft =
      orangeRepository
          .getAllEditorState()
          .stateIn(
              scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(5000),
              initialValue = emptyList(),
          )

  private val editorRender = EditorRenderer()

  private val undoStack = mutableListOf<List<Layer>>()
  private val redoStack = mutableListOf<List<Layer>>()
  private val maxHistorySize = 20

  fun resetState() {
    _editorState.update { it.copy(layers = emptyList(), selectedLayerId = null) }
  }

  fun addLayer(layer: Layer) {
    saveSnapshot()
    _editorState.update { it.copy(layers = it.layers + layer, selectedLayerId = layer.id) }
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
            adjustment = NeutralAdjustment,
            transform = Transform(x = x, y = y, scale = scale, rotation = 0f),
            zIndex = (_editorState.value.layers.maxOfOrNull { it.zIndex } ?: 0) + 1,
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
            zIndex = (_editorState.value.layers.maxOfOrNull { it.zIndex } ?: 0) + 1,
            visible = true,
        )

    addLayer(layer)
  }

  fun getSelectedLayer(): Layer? {
    return _editorState.value.layers.firstOrNull { it.id == _editorState.value.selectedLayerId }
  }

  fun getSelectedImagerLayer(): ImageLayer? {
    val currentSelectedLayer = getSelectedLayer()
    if (currentSelectedLayer is ImageLayer) return currentSelectedLayer
    return null
  }

  fun getSelectedTextLayer(): TextLayer? {
    val currentSelectedLayer = getSelectedLayer()
    if (currentSelectedLayer is TextLayer) return currentSelectedLayer
    return null
  }

  fun updateLayer(updatedLayer: Layer) {
    _editorState.update { state ->
      state.copy(
          layers =
              state.layers.map { layer -> if (layer.id == updatedLayer.id) updatedLayer else layer }
      )
    }
  }

  fun updateBitmapOfSelectedImageLayer(updatedBitmap: Bitmap) {
    val selectedLayer = getSelectedLayer()
    val selectedImageLayer = selectedLayer as? ImageLayer
    selectedImageLayer?.let {
      saveSnapshot()
      updateLayer(updatedLayer = it.copy(bitmap = updatedBitmap))
    }
  }

  fun updateImageFilterOfSelectedImagerLayer(imageFilter: ImageFilter) {
    val selectedLayer = getSelectedLayer()
    val selectedImageLayer = selectedLayer as? ImageLayer
    selectedImageLayer?.let {
      saveSnapshot()
      updateLayer(updatedLayer = it.copy(imageFilter = imageFilter, adjustment = NeutralAdjustment))
    }
  }

  fun updateAdjustmentsOfSelectedImageLayer(adjustment: Adjustment) {
    val selectedLayer = getSelectedLayer()
    val selectedImageLayer = selectedLayer as? ImageLayer
    selectedImageLayer?.let {
      updateLayer(
          updatedLayer = it.copy(adjustment = adjustment, imageFilter = ImageFilter.NO_FILTER)
      )
    }
  }

  fun updateSelectedTextLayer(
      text: String,
      fontSizeInPx: Int,
      fontColor: Color,
      fontWeight: FontWeight,
      fontStyle: FontStyle,
  ) {
    val selectedTextLayer = getSelectedTextLayer() ?: return

    saveSnapshot()

    val updatedLayer =
        selectedTextLayer.copy(
            text = text,
            fontSizeInPx = fontSizeInPx,
            color = fontColor,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
        )

    updateLayer(updatedLayer)
  }

  fun removeLayer(id: String) {
    saveSnapshot()
    _editorState.update { state ->
      state.copy(layers = state.layers.filterNot { it.id == id }, selectedLayerId = null)
    }
  }

  fun updateFileName(newName: String) {
    val sanitized = newName.replace(Regex("[/\\\\:*?\"<>|]"), "").trim().take(20)

    val finalName = sanitized.ifEmpty { "Draft" }

    _editorState.update { it.copy(fileName = finalName) }
  }

  private fun saveSnapshot() {
    val currentLayers = _editorState.value.layers.toList()

    undoStack.add(currentLayers)

    if (undoStack.size > maxHistorySize) {
      undoStack.removeAt(0)
    }

    redoStack.clear()
  }

  fun canUndo(): Boolean {
    return undoStack.isNotEmpty()
  }

  fun canRedo(): Boolean {
    return redoStack.isNotEmpty()
  }

  fun undo() {
    if (!canUndo()) return

    val currentLayers = _editorState.value.layers.toList()
    redoStack.add(currentLayers)

    val previousLayers = undoStack.removeAt(undoStack.lastIndex)
    _editorState.update { it.copy(layers = previousLayers, selectedLayerId = null) }
  }

  fun redo() {
    if (!canRedo()) return

    val currentLayers = _editorState.value.layers.toList()
    undoStack.add(currentLayers)

    val nextLayers = redoStack.removeAt(redoStack.lastIndex)
    _editorState.update { it.copy(layers = nextLayers, selectedLayerId = null) }
  }

  fun exportImage(context: Context, canvasFormat: CanvasFormat, canvasScreenSize: IntSize) {
    val currentLayers = _editorState.value.layers
    val currentFileName = _editorState.value.fileName

    if (currentLayers.isEmpty()) {
      Toast.makeText(context, "Nothing to export", Toast.LENGTH_SHORT).show()
      return
    }

    viewModelScope.launch {
      try {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "${currentFileName}_${timestamp}.png"
        val scaleX = canvasFormat.width.toFloat() / canvasScreenSize.width
        val scaleY = canvasFormat.height.toFloat() / canvasScreenSize.height

        val exportBitmap = createBitmap(canvasFormat.width, canvasFormat.height)
        val canvas = Canvas(exportBitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        currentLayers
            .sortedBy { it.zIndex }
            .filter { it.visible }
            .forEach { layer ->
              when (layer) {
                is ImageLayer -> editorRender.drawImage(canvas, layer, scaleX, scaleY)
                is TextLayer -> editorRender.drawText(canvas, layer, scaleX, scaleY)
              }
            }

        val saved = saveBitmapToDownloads(context, exportBitmap, fileName)
        exportBitmap.recycle()

        Toast.makeText(
                context,
                if (saved) "Exported successfully!" else "Export failed",
                Toast.LENGTH_LONG,
            )
            .show()
      } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun saveBitmapToDownloads(context: Context, bitmap: Bitmap, fileName: String): Boolean {
    return try {
      val resolver = context.contentResolver

      val contentValues =
          ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_DOWNLOADS}/OrangeEditor",
            )
          }

      val imageUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

      imageUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        true
      } ?: false
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  fun saveDraft() {
    viewModelScope.launch { orangeRepository.saveEditorState(editorState = _editorState.value) }
  }

  fun selectedDraft(editorId: String) {
    viewModelScope.launch {
      val draft = orangeRepository.getEditorStatById(editorId = editorId)
      _editorState.update { draft }
    }
  }

  fun deleteSavedDraft(editorId: String) {
    viewModelScope.launch { orangeRepository.deleteEditorStateById(editorId = editorId) }
  }

  fun newEditorState(canvasFormat: CanvasFormat) {
    _editorState.value = EditorState(canvasFormat = canvasFormat)
  }
}
