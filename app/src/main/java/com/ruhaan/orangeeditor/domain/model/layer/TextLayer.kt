package com.ruhaan.orangeeditor.domain.model.layer

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.ruhaan.orangeeditor.Constant.TEXT_HIT_PADDING_PX
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

data class TextLayer(
    override val id: String,
    val displayName: String,
    val text: String,
    val color: Color,
    val fontSizeInPx: Int,
    /** This only support only Normal and Bold. */
    val fontWeight: FontWeight,
    /** This only support only Normal and Italic. */
    val fontStyle: FontStyle,
    override val transform: Transform,
    override val zIndex: Int,
    override val visible: Boolean = true,
    val textWidthPx: Int = 0,
    val textHeightPx: Int = 0,
) : Layer(id = id, transform = transform, zIndex = zIndex, visible = visible)

fun TextLayer.isIntersect(tapX: Float, tapY: Float): Boolean {
  val t = transform

  var x = tapX - t.x
  var y = tapY - t.y

  val rad = toRadians(-t.rotation.toDouble())
  val cos = cos(rad)
  val sin = sin(rad)

  val rx = (x * cos - y * sin).toFloat()
  val ry = (x * sin + y * cos).toFloat()

  x = rx / t.scale
  y = ry / t.scale

  val halfW = textWidthPx / 2f
  val halfH = textHeightPx / 2f

  Log.i("LOG Layer", "local = $x , $y")

  return x in -(halfW + TEXT_HIT_PADDING_PX)..(halfW + TEXT_HIT_PADDING_PX) &&
      y in -(halfH + TEXT_HIT_PADDING_PX)..(halfH + TEXT_HIT_PADDING_PX)
}
