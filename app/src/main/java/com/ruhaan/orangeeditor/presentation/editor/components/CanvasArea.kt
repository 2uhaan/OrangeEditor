package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CanvasArea(width: Dp = 360.dp, height: Dp = 360.dp) {
  Box(
      modifier =
          Modifier.border(width = 2.dp, color = Color.Gray.copy(alpha = .4f))
              .size(width = width, height = height),
      contentAlignment = Alignment.Center,
  ) {
    Surface(shadowElevation = 2.dp, color = Color.White) {
      Canvas(modifier = Modifier.size(width = width, height = height)) {}
    }
  }
}
