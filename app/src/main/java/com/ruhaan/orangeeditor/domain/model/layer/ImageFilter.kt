package com.ruhaan.orangeeditor.domain.model.layer

import android.graphics.ColorMatrix

enum class ImageFilter(val colorMatrix: ColorMatrix) {

  NO_FILTER(
    ColorMatrix(
      floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
      )
    )
  ),

  GRAYSCALE(
    ColorMatrix(
      floatArrayOf(
        0.299f, 0.587f, 0.114f, 0f, 0f,
        0.299f, 0.587f, 0.114f, 0f, 0f,
        0.299f, 0.587f, 0.114f, 0f, 0f,
        0f,     0f,     0f,     1f, 0f
      )
    )
  ),

  SEPIA(
    ColorMatrix(
      floatArrayOf(
        0.393f, 0.769f, 0.189f, 0f, 0f,
        0.349f, 0.686f, 0.168f, 0f, 0f,
        0.272f, 0.534f, 0.131f, 0f, 0f,
        0f,     0f,     0f,     1f, 0f
      )
    )
  ),

  INVERT(
    ColorMatrix(
      floatArrayOf(
        -1f,  0f,  0f,  0f, 255f,
        0f, -1f,  0f,  0f, 255f,
        0f,  0f, -1f,  0f, 255f,
        0f,  0f,  0f,  1f,   0f
      )
    )
  ),

  BRIGHT(
    ColorMatrix(
      floatArrayOf(
        1f, 0f, 0f, 0f, 40f,
        0f, 1f, 0f, 0f, 40f,
        0f, 0f, 1f, 0f, 40f,
        0f, 0f, 0f, 1f, 0f
      )
    )
  ),

  HIGH_CONTRAST(
    ColorMatrix(
      floatArrayOf(
        1.5f, 0f,   0f,   0f, -50f,
        0f,   1.5f, 0f,   0f, -50f,
        0f,   0f,   1.5f, 0f, -50f,
        0f,   0f,   0f,   1f,   0f
      )
    )
  ),

  WARM(
    ColorMatrix(
      floatArrayOf(
        1.1f, 0f,   0f,   0f, 10f,
        0f,   1.05f,0f,   0f, 5f,
        0f,   0f,   0.95f,0f, 0f,
        0f,   0f,   0f,   1f, 0f
      )
    )
  ),

  COOL(
    ColorMatrix(
      floatArrayOf(
        0.95f, 0f,   0f,   0f, 0f,
        0f,    1f,   0f,   0f, 0f,
        0f,    0f,   1.1f, 0f, 10f,
        0f,    0f,   0f,   1f, 0f
      )
    )
  );

}
