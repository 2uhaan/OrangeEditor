package com.ruhaan.orangeeditor.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppStorage(private val context: Context) {
  suspend fun saveBitmapToAppStorage(
      bitmap: Bitmap,
      fileName: String = "img_${System.currentTimeMillis()}.png",
      format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
      quality: Int = 100,
  ): String? =
      withContext(Dispatchers.IO) {
        try {
          val dir = File(context.filesDir, "images").apply { if (!exists()) mkdirs() }
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
}
