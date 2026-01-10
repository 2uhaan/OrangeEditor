package com.ruhaan.orangeeditor.util

import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.core.graphics.withSave
import com.ruhaan.orangeeditor.domain.model.layer.ImageFilter
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.domain.model.layer.NeutralAdjustment
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.model.layer.toColorMatrix

class EditorRenderer {

  fun draw(canvas: Canvas, layers: List<Layer>) {
    layers
        .sortedBy { it.zIndex }
        .filter { it.visible }
        .forEach { layer ->
          when (layer) {
            is TextLayer -> drawText(canvas, layer)
            is ImageLayer -> drawImage(canvas, layer)
          }
        }
  }

  fun drawImage(
      canvas: Canvas,
      layer: ImageLayer,
      scaleX: Float = 1f,
      scaleY: Float = 1f,
  ): Canvas {
    layer.bitmap?.let {
      canvas.withSave {
        val t = layer.transform
        translate(t.x * scaleX, t.y * scaleY)
        rotate(t.rotation)
        scale(t.scale * scaleX, t.scale * scaleY)

        val filter = layer.imageFilter

        val isApplyCustomAdjustments = layer.adjustment != NeutralAdjustment

        val paint =
            Paint().apply {
              colorFilter =
                  ColorMatrixColorFilter(
                      if (isApplyCustomAdjustments) layer.adjustment.toColorMatrix()
                      else layer.imageFilter.colorMatrix
                  )
            }

        val shouldApplyPaint = isApplyCustomAdjustments || filter != ImageFilter.NO_FILTER

        drawBitmap(
            layer.bitmap,
            -layer.bitmap.width / 2f,
            -layer.bitmap.height / 2f,
            if (shouldApplyPaint) paint else null,
        )
      }
    }
    return canvas
  }

  fun drawText(
      canvas: Canvas,
      layer: TextLayer,
      scaleX: Float = 1f,
      scaleY: Float = 1f,
  ) {
    canvas.withSave {
      val t = layer.transform
      translate(t.x * scaleX, t.y * scaleY)
      rotate(t.rotation)
      scale(t.scale * scaleX, t.scale * scaleY)

      val paint =
          Paint().apply {
            color = layer.color.toArgb()
            textSize = layer.fontSizeInPx.toFloat()
            isAntiAlias = true
            typeface = resolveTypeface(layer.fontWeight, layer.fontStyle)
          }

      drawText(layer.text, 0f, 0f, paint)
    }
  }

  private fun resolveTypeface(
      fontWeight: FontWeight,
      fontStyle: FontStyle,
  ): Typeface {

    val style =
        when {
          fontWeight >= FontWeight.Bold && fontStyle == FontStyle.Italic -> Typeface.BOLD_ITALIC
          fontWeight >= FontWeight.Bold -> Typeface.BOLD
          fontStyle == FontStyle.Italic -> Typeface.ITALIC
          else -> Typeface.NORMAL
        }

    return Typeface.create(Typeface.DEFAULT, style)
  }
}
