package com.ruhaan.orangeeditor.domain.model.layer

import android.graphics.ColorMatrix
import com.ruhaan.orangeeditor.R

enum class ImageFilter(val previewName : String, val colorMatrix: ColorMatrix,  val previewImageId : Int) {

  NO_FILTER(
    "No filter",
    ColorMatrix(
      floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
      )
    ),
    R.drawable.img_no_filter
  ),

  GRAYSCALE("Grayscale",
    ColorMatrix(
      floatArrayOf(
        0.299f, 0.587f, 0.114f, 0f, 0f,
        0.299f, 0.587f, 0.114f, 0f, 0f,
        0.299f, 0.587f, 0.114f, 0f, 0f,
        0f,     0f,     0f,     1f, 0f
      )
    ),
    R.drawable.img_grayscale
  ),

  SEPIA("Sepia",
    ColorMatrix(
      floatArrayOf(
        0.393f, 0.769f, 0.189f, 0f, 0f,
        0.349f, 0.686f, 0.168f, 0f, 0f,
        0.272f, 0.534f, 0.131f, 0f, 0f,
        0f,     0f,     0f,     1f, 0f
      )
    ),
    R.drawable.img_sepia
  ),

  INVERT("Invert",
    ColorMatrix(
      floatArrayOf(
        -1f,  0f,  0f,  0f, 255f,
        0f, -1f,  0f,  0f, 255f,
        0f,  0f, -1f,  0f, 255f,
        0f,  0f,  0f,  1f,   0f
      )
    ),
    R.drawable.img_invert
  ),

  BRIGHT("Bright",
    ColorMatrix(
      floatArrayOf(
        1f, 0f, 0f, 0f, 40f,
        0f, 1f, 0f, 0f, 40f,
        0f, 0f, 1f, 0f, 40f,
        0f, 0f, 0f, 1f, 0f
      )
    ),
    R.drawable.img_bright
  ),

  HIGH_CONTRAST("High contrast",
    ColorMatrix(
      floatArrayOf(
        1.5f, 0f,   0f,   0f, -50f,
        0f,   1.5f, 0f,   0f, -50f,
        0f,   0f,   1.5f, 0f, -50f,
        0f,   0f,   0f,   1f,   0f
      )
    ),
    R.drawable.img_high_contrast
  ),

  WARM("Warm",
    ColorMatrix(
      floatArrayOf(
        1.1f, 0f,   0f,   0f, 10f,
        0f,   1.05f,0f,   0f, 5f,
        0f,   0f,   0.95f,0f, 0f,
        0f,   0f,   0f,   1f, 0f
      )
    ),
    R.drawable.img_warm
  ),

  COOL("Cool",
    ColorMatrix(
      floatArrayOf(
        0.95f, 0f,   0f,   0f, 0f,
        0f,    1f,   0f,   0f, 0f,
        0f,    0f,   1.1f, 0f, 10f,
        0f,    0f,   0f,   1f, 0f
      )
    ),
    R.drawable.img_cool
  );

}
