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
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange

class EditorRenderer {

  fun draw(
      canvas: Canvas,
      layers: List<Layer>,
      scaleX: Float = 1f,
      scaleY: Float = 1f,
      selectedLayerId: String? = null,
      onTextMeasured: (TextLayer) -> Unit,
  ) {
    layers
        .sortedBy { it.zIndex }
        .filter { it.visible }
        .forEach { layer ->
          when (layer) {
            is TextLayer -> drawText(canvas, layer, scaleX, scaleY, selectedLayerId, onTextMeasured)
            is ImageLayer -> drawImage(canvas, layer, scaleX, scaleY, selectedLayerId)
          }
        }
  }

  fun drawImage(
      canvas: Canvas,
      layer: ImageLayer,
      scaleX: Float = 1f,
      scaleY: Float = 1f,
      selectedLayerId: String? = null,
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

        if (selectedLayerId == layer.id) {
          val borderPaint =
              Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 8f
                color = CanvasOrange.toArgb()
                isAntiAlias = true
              }

          // Image bounds (center-anchored)
          val halfW = layer.bitmap.width / 2f
          val halfH = layer.bitmap.height / 2f

          drawRect(-(halfW + 10), -(halfH + 10), (halfW + 10), (halfH + 10), borderPaint)
        }
      }
    }
    return canvas
  }

  fun drawText(
      canvas: Canvas,
      layer: TextLayer,
      scaleX: Float = 1f,
      scaleY: Float = 1f,
      selectedLayerId: String? = null,
      onTextMeasured: ((TextLayer) -> Unit)? = null,
  ) {
    val paint =
        Paint().apply {
          color = layer.color.toArgb()
          textSize = layer.fontSizeInPx.toFloat()
          isAntiAlias = true
          typeface = resolveTypeface(layer.fontWeight, layer.fontStyle)
        }

    val bounds = android.graphics.Rect()
    paint.getTextBounds(layer.text, 0, layer.text.length, bounds)

    val newWidth = bounds.width()
    val newHeight = bounds.height()

    if (
        newWidth > 0 &&
            newHeight > 0 &&
            (layer.textWidthPx != newWidth || layer.textHeightPx != newHeight)
    ) {
      onTextMeasured?.invoke(layer.copy(textWidthPx = newWidth, textHeightPx = newHeight))
    }

    canvas.withSave {
      val t = layer.transform
      translate(t.x * scaleX, t.y * scaleY)
      rotate(t.rotation)
      scale(scaleX, scaleY)

      drawText(layer.text, 0f, -bounds.top.toFloat(), paint)

      if (selectedLayerId == layer.id) {
        val borderPaint =
            Paint().apply {
              style = Paint.Style.STROKE
              strokeWidth = 4f
              color = CanvasOrange.toArgb()
              isAntiAlias = true
            }

        drawRect(0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(), borderPaint)
      }
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
