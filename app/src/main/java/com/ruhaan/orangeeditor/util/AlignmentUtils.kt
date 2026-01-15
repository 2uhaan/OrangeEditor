package com.ruhaan.orangeeditor.util

import kotlin.math.abs

fun snapToGuides(
    value: Float,
    canvasWidth: Float = 0f,
    canvasHeight: Float = 0f,
    layerWidth: Float = 0f,
    layerHeight: Float = 0f,
    threshold: Float,
): Float {
  val centerX = canvasWidth / 2f
  val centerY = canvasHeight / 2f

  val layerLeft = value - layerWidth / 2f
  val layerRight = value + layerWidth / 2f
  val layerTop = value - layerHeight / 2f

  // Snap to Line
  if (abs(value - centerX) <= threshold) return centerX

  if (abs(layerLeft - 0f) <= threshold) return layerWidth / 2f

  if (abs(layerRight - canvasWidth) <= threshold) return canvasWidth - layerWidth / 2f

  if (abs(layerTop - 0f) <= threshold) return layerHeight / 2f

  return value
}
