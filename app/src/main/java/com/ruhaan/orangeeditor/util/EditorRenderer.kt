package com.ruhaan.orangeeditor.util

import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.withSave
import com.ruhaan.orangeeditor.domain.model.layer.Adjustments
import com.ruhaan.orangeeditor.domain.model.layer.EmojiLayer
import com.ruhaan.orangeeditor.domain.model.layer.ImageFilter
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.toColorMatrix

class EditorRenderer {

  fun draw(canvas: Canvas, layers: List<Layer>) {
    layers
        .sortedBy { it.zIndex }
        .filter { it.visible }
        .forEach { layer ->
          when (layer) {
            is EmojiLayer -> drawEmoji(canvas, layer)
            is TextLayer -> drawText(canvas, layer)
            is ImageLayer -> drawImage(canvas, layer)
          }
        }
  }

  private fun drawImage(canvas: Canvas, layer: ImageLayer) {
    canvas.withSave {
      val t = layer.transform
      translate(t.x, t.y)
      rotate(t.rotation)
      scale(t.scale, t.scale)

      val filter = layer.imageFilter

      val isApplyCustomAdjustments = layer.adjustments == Adjustments()

      val paint =
          Paint().apply {
            colorFilter =
                ColorMatrixColorFilter(
                    if (isApplyCustomAdjustments) layer.adjustments.toColorMatrix()
                    else layer.imageFilter.colorMatrix
                )
          }

      drawBitmap(
          layer.bitmap,
          -layer.bitmap.width / 2f,
          -layer.bitmap.height / 2f,
          if (filter == ImageFilter.NO_FILTER) null else paint,
      )
    }
  }

  private fun drawEmoji(canvas: Canvas, layer: EmojiLayer) {
    canvas.withSave {
      val t = layer.transform
      translate(t.x, t.y)
      rotate(t.rotation)
      scale(t.scale, t.scale)

      drawBitmap(layer.bitmap, -layer.bitmap.width / 2f, -layer.bitmap.height / 2f, null)
    }
  }

  private fun drawText(canvas: Canvas, layer: TextLayer) {
    canvas.withSave {
      val t = layer.transform
      translate(t.x, t.y)
      rotate(t.rotation)
      scale(t.scale, t.scale)

      val paint =
          Paint().apply {
            color = layer.color.toArgb()
            textSize = layer.fontSizeInPx
            isAntiAlias = true
          }

      drawText(layer.text, 0f, 0f, paint)
    }
  }
}
