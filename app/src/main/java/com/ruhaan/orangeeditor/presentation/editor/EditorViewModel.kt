package com.ruhaan.orangeeditor.presentation.editor

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.domain.model.layer.Adjustments
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.ImageFilter
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.NeutralAdjustments
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.Transform
import com.ruhaan.orangeeditor.domain.model.layer.toColorMatrix
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
class EditorViewModel : ViewModel() {

  private val _state = MutableStateFlow(EditorState())
  val state = _state.asStateFlow()

  private val undoStack = mutableListOf<List<Layer>>()
  private val redoStack = mutableListOf<List<Layer>>()
  private val maxHistorySize = 20

  fun resetState() {
    _state.update { it.copy(layers = emptyList(), selectedLayerId = null) }
  }

  fun addLayer(layer: Layer) {
    saveSnapshot()
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

  fun getSelectedLayer(): Layer? {
    return _state.value.layers.firstOrNull { it.id == _state.value.selectedLayerId }
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
    _state.update { state ->
      state.copy(
          layers =
              state.layers.map { layer -> if (layer.id == updatedLayer.id) updatedLayer else layer }
      )
    }
    Log.i("LOG", "${_state.value.layers}")
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
      updateLayer(
          updatedLayer = it.copy(imageFilter = imageFilter, adjustments = NeutralAdjustments)
      )
    }
  }

  fun updateAdjustmentsOfSelectedImageLayer(adjustments: Adjustments) {
    val selectedLayer = getSelectedLayer()
    val selectedImageLayer = selectedLayer as? ImageLayer
    selectedImageLayer?.let {
      updateLayer(
          updatedLayer = it.copy(adjustments = adjustments, imageFilter = ImageFilter.NO_FILTER)
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
    _state.update { state -> state.copy(layers = state.layers.filterNot { it.id == id }) }
  }

  fun updateFileName(newName: String) {
    val sanitized = newName.replace(Regex("[/\\\\:*?\"<>|]"), "").trim().take(20)

    val finalName = sanitized.ifEmpty { "Draft" }

    _state.update { it.copy(fileName = finalName) }
  }

  private fun saveSnapshot() {
    val currentLayers = _state.value.layers.toList()

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

    val currentLayers = _state.value.layers.toList()
    redoStack.add(currentLayers)

    val previousLayers = undoStack.removeAt(undoStack.lastIndex)
    _state.update { it.copy(layers = previousLayers, selectedLayerId = null) }
  }

  fun redo() {
    if (!canRedo()) return

    val currentLayers = _state.value.layers.toList()
    undoStack.add(currentLayers)

    val nextLayers = redoStack.removeAt(redoStack.lastIndex)
    _state.update { it.copy(layers = nextLayers, selectedLayerId = null) }
  }

  fun exportImage(context: Context, canvasFormat: CanvasFormat, canvasScreenSize: IntSize) {
    val currentLayers = _state.value.layers
    val currentFileName = _state.value.fileName

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

        val exportBitmap =
            Bitmap.createBitmap(canvasFormat.width, canvasFormat.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(exportBitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        currentLayers
            .sortedBy { it.zIndex }
            .filter { it.visible }
            .forEach { layer ->
              when (layer) {
                is ImageLayer -> drawImageLayerForExport(canvas, layer, scaleX, scaleY)
                is TextLayer -> drawTextLayerForExport(canvas, layer, scaleX, scaleY)
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

  private fun applyFilterToBitmap(bitmap: Bitmap, layer: ImageLayer): Bitmap {
    val softwareBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true) ?: bitmap

    val filtered =
        Bitmap.createBitmap(softwareBitmap.width, softwareBitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(filtered)

    val colorMatrix =
        if (layer.adjustments != NeutralAdjustments) {
          layer.adjustments.toColorMatrix()
        } else {
          layer.imageFilter.colorMatrix
        }

    val paint =
        android.graphics.Paint().apply {
          colorFilter = android.graphics.ColorMatrixColorFilter(colorMatrix)
        }

    canvas.drawBitmap(softwareBitmap, 0f, 0f, paint)
    return filtered
  }

  private fun drawImageLayerForExport(
      canvas: Canvas,
      layer: ImageLayer,
      scaleX: Float,
      scaleY: Float,
  ) {
    val processedBitmap = applyFilterToBitmap(layer.bitmap, layer)

    canvas.save()
    canvas.translate(layer.transform.x * scaleX, layer.transform.y * scaleY)
    canvas.rotate(layer.transform.rotation)
    canvas.scale(layer.transform.scale * scaleX, layer.transform.scale * scaleY)
    canvas.drawBitmap(
        processedBitmap,
        -processedBitmap.width / 2f,
        -processedBitmap.height / 2f,
        null,
    )
    canvas.restore()

    processedBitmap.recycle()
  }

  private fun drawTextLayerForExport(
      canvas: Canvas,
      layer: TextLayer,
      scaleX: Float,
      scaleY: Float,
  ) {
    val paint =
        android.graphics.Paint().apply {
          color = layer.color.toArgb()
          textSize = layer.fontSizeInPx * scaleX
          isAntiAlias = true
          typeface =
              when {
                layer.fontWeight >= FontWeight.Bold && layer.fontStyle == FontStyle.Italic ->
                    android.graphics.Typeface.BOLD_ITALIC
                layer.fontWeight >= FontWeight.Bold -> android.graphics.Typeface.BOLD
                layer.fontStyle == FontStyle.Italic -> android.graphics.Typeface.ITALIC
                else -> android.graphics.Typeface.NORMAL
              }.let { android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, it) }
        }

    canvas.save()
    canvas.translate(layer.transform.x * scaleX, layer.transform.y * scaleY)
    canvas.rotate(layer.transform.rotation)
    canvas.scale(layer.transform.scale * scaleX, layer.transform.scale * scaleY)
    canvas.drawText(layer.text, -paint.measureText(layer.text) / 2f, 0f, paint)
    canvas.restore()
  }
}
