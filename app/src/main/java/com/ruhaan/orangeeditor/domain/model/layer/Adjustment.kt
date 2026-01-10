package com.ruhaan.orangeeditor.domain.model.layer

import android.graphics.ColorMatrix
import kotlin.math.pow

/**
 * All values are CLAMPED internally to safe ranges.
 *
 * Ranges:
 * - Saturation: 0f .. 2f (1 = normal)
 * - Brightness: -100 .. +100 (0 = normal)
 * - Contrast: 0.5f .. 1.5f (1 = normal)
 * - Exposure: -1f .. +1f (0 = normal)
 * - Temperature: -100 .. +100 (0 = neutral)
 * - Tint: -100 .. +100 (0 = neutral)
 * - Hue: -180 .. +180 (0 = normal)
 */
data class Adjustment(
    val saturation: Float,
    val brightness: Float,
    val contrast: Float,
    val exposure: Float,
    val temperature: Float,
    val tint: Float,
    val hue: Float,
)

val NeutralAdjustment =
    Adjustment(
        saturation = 1f,
        brightness = 0f,
        contrast = 1f,
        exposure = 0f,
        temperature = 0f,
        tint = 0f,
        hue = 0f,
    )

fun Adjustment.toColorMatrix(): ColorMatrix {

  // ---------- Clamp helpers ----------
  fun clamp(v: Float, min : Float, max: Float) = v.coerceIn(min, max)

  val s = clamp(v = saturation,min = 0f, max = 2f)
  val b = clamp(v = brightness, min = -100f, max = 100f)
  val c = clamp(v = contrast, min = 0.5f, max = 1.5f)
  val e = clamp(v = exposure, min = -1f, max = 1f)
  val t = clamp(v = temperature, min = -100f, max = 100f)
  val ti = clamp(v = tint, min = -100f, max = 100f)
  val h = clamp(v = hue, min = -180f, max = 180f)

  val result = ColorMatrix()

  // ---------- Saturation ----------
  result.postConcat(
    ColorMatrix().apply { setSaturation(s) }
  )

  // ---------- Contrast ----------
  val contrastTranslate = (-0.5f * c + 0.5f) * 255f
  result.postConcat(
    ColorMatrix(
      floatArrayOf(
        c, 0f, 0f, 0f, contrastTranslate,
        0f, c, 0f, 0f, contrastTranslate,
        0f, 0f, c, 0f, contrastTranslate,
        0f, 0f, 0f, 1f, 0f
      )
    )
  )

  // ---------- Exposure ----------
  val exposureFactor = 2f.pow(e)
  result.postConcat(
    ColorMatrix(
      floatArrayOf(
        exposureFactor, 0f, 0f, 0f, 0f,
        0f, exposureFactor, 0f, 0f, 0f,
        0f, 0f, exposureFactor, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
      )
    )
  )

  // ---------- Temperature (Warmth) ----------
  result.postConcat(
    ColorMatrix(
      floatArrayOf(
        1f + t / 200f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f - t / 200f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
      )
    )
  )

  // ---------- Tint (Green ↔ Magenta) ----------
  result.postConcat(
    ColorMatrix(
      floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f + ti / 200f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
      )
    )
  )

  // ---------- Hue ----------
  val hueMatrix = ColorMatrix()
  hueMatrix.setRotate(0, h)
  result.postConcat(hueMatrix)

  hueMatrix.reset()
  hueMatrix.setRotate(1, h)
  result.postConcat(hueMatrix)

  hueMatrix.reset()
  hueMatrix.setRotate(2, h)
  result.postConcat(hueMatrix)

  // ---------- Brightness (LAST) ----------
  result.postConcat(
    ColorMatrix(
      floatArrayOf(
        1f, 0f, 0f, 0f, b,
        0f, 1f, 0f, 0f, b,
        0f, 0f, 1f, 0f, b,
        0f, 0f, 0f, 1f, 0f
      )
    )
  )

  return result
}
