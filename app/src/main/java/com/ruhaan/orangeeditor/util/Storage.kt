package com.ruhaan.orangeeditor.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.createBitmap
import com.ruhaan.orangeeditor.data.storage.StorageType
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Storage(private val context: Context, private val editorRenderer: EditorRenderer) {
  suspend fun saveBitmapToAppStorage(
      bitmap: Bitmap,
      storageType: StorageType,
      fileName: String = "img_${System.currentTimeMillis()}.jpg",
      format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
      quality: Int = 80,
  ): String? =
      withContext(Dispatchers.IO) {
        try {
          val dir = File(context.filesDir, storageType.folderName).apply { if (!exists()) mkdirs() }
          val file = File(dir, fileName)
          FileOutputStream(file).use { out -> bitmap.compress(format, quality, out) }
          file.absolutePath
        } catch (e: Exception) {
          e.printStackTrace()
          null
        }
      }

  suspend fun loadBitmapFromPath(path: String): Bitmap? =
      withContext(Dispatchers.IO) { BitmapFactory.decodeFile(path) }

  suspend fun deleteBitmapFromPath(path: String) {
    withContext(Dispatchers.IO) {
      val file = File(path)
      if (file.exists()) {
        file.delete()
      }
    }
  }

  fun getBitmapFromLayer(
      layers: List<Layer>,
      canvasFormat: CanvasFormat,
      canvasScreenSize: IntSize,
  ): Bitmap {
    val exportBitmap = createBitmap(canvasFormat.width, canvasFormat.height)
    val canvas = Canvas(exportBitmap)
    canvas.drawColor(android.graphics.Color.WHITE)

    if (layers.isEmpty()) {
      return exportBitmap
    }

    try {
      val scaleX = canvasFormat.width.toFloat() / canvasScreenSize.width
      val scaleY = canvasFormat.height.toFloat() / canvasScreenSize.height

      canvas.save()
      editorRenderer.draw(canvas, layers, scaleX, scaleY)

      return exportBitmap
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
    }
    return exportBitmap
  }

  fun saveBitmapToDownloads(context: Context, bitmap: Bitmap, fileName: String): Uri? {
    return try {
      val resolver = context.contentResolver

      val contentValues =
          ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_DOWNLOADS}/${StorageType.DOWNLOAD_DIR.folderName}",
            )
          }

      val imageUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

      imageUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
      }
      imageUri
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }
}
