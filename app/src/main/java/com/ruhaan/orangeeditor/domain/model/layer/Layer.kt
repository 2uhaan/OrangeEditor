package com.ruhaan.orangeeditor.domain.model.layer

import android.graphics.Bitmap
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

sealed class Layer(
    open val id: String,
    open val transform: Transform,
    open val zIndex: Int,
    open val visible: Boolean = true,
    open val bitmap: Bitmap?,
)

fun Layer.isIntersect(tapX: Float, tapY: Float): Boolean {
  val bmp = bitmap ?: return false

  val t = transform

  // 1️⃣ Translate into layer space
  var x = tapX - t.x
  var y = tapY - t.y

  // 2️⃣ Inverse rotation
  val rad = toRadians(-t.rotation.toDouble())
  val cos = cos(rad)
  val sin = sin(rad)

  val rx = (x * cos - y * sin).toFloat()
  val ry = (x * sin + y * cos).toFloat()

  // 3️⃣ Inverse scale
  x = rx / t.scale
  y = ry / t.scale

  // 4️⃣ Center-anchored bounds
  val halfW = bmp.width / 2f
  val halfH = bmp.height / 2f

  return x in -halfW..halfW && y in -halfH..halfH
}
