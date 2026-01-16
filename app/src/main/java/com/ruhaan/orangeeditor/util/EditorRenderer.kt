package com.ruhaan.orangeeditor.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.core.graphics.createBitmap
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
  ) {
    layers
        .sortedBy { it.zIndex }
        .filter { it.visible }
        .forEach { layer ->
          when (layer) {
            is TextLayer -> drawBitmap(canvas, layer, scaleX, scaleY, selectedLayerId)
            is ImageLayer -> drawBitmap(canvas, layer, scaleX, scaleY, selectedLayerId)
          }
        }
  }

  fun drawBitmap(
      canvas: Canvas,
      layer: Layer,
      scaleX: Float = 1f,
      scaleY: Float = 1f,
      selectedLayerId: String? = null,
  ): Canvas {
    layer.bitmap?.let { newBitmap ->
      canvas.withSave {
        val t = layer.transform
        translate(t.x * scaleX, t.y * scaleY)
        rotate(t.rotation)
        scale(t.scale * scaleX, t.scale * scaleY)

        val (paint, shouldApplyPaint) =
            when (layer) {
              is ImageLayer -> {
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

                paint to shouldApplyPaint
              }

              is TextLayer -> {
                null to false
              }
            }

        drawBitmap(
            newBitmap,
            -newBitmap.width / 2f,
            -newBitmap.height / 2f,
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
          val halfW = newBitmap.width / 2f
          val halfH = newBitmap.height / 2f

          drawRect(-(halfW + 10), -(halfH + 10), (halfW + 10), (halfH + 10), borderPaint)
        }
      }
    }
    return canvas
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

  fun textLayerToBitmap(
      text: String,
      color: Color,
      fontWeight: FontWeight,
      fontStyle: FontStyle,
  ): Bitmap {
    val paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
          this.color = color.toArgb()
          textSize = 80.toFloat()
          typeface = resolveTypeface(fontWeight, fontStyle)
          textAlign = Paint.Align.LEFT
        }

    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)

    val width = bounds.width().coerceAtLeast(1)
    val height = bounds.height().coerceAtLeast(1)

    val bitmap = createBitmap(width, height)

    val canvas = Canvas(bitmap)

    // IMPORTANT: align text exactly to bitmap
    canvas.drawText(text, -bounds.left.toFloat(), -bounds.top.toFloat(), paint)

    return bitmap
  }
}
