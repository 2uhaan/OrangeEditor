package com.ruhaan.orangeeditor.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AspectRatioPreview(aspectRatio: String, modifier: Modifier = Modifier) {

  Box(
      modifier =
          modifier
              .background(
                  brush =
                      Brush.linearGradient(
                          colors = listOf(Color(0xFFF47D34), Color(0xFFFFFFFF)),
                          start = Offset(0f, 0f),
                          end = Offset(800f, 800f),
                      )
              )
              .size(calculateDpSize(aspectRatio)),
      contentAlignment = Alignment.Center,
  ) {
    Text(text = aspectRatio, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
  }
}

private fun calculateDpSize(aspectRatio: String): DpSize {
  val base = 80f

  return when (aspectRatio) {
    "9:16" -> DpSize(width = (base * 0.7f).dp, height = (base * 1.42f).dp)
    "4:5" -> DpSize(width = (base * 0.9f).dp, height = (base * 1.125f).dp)
    "16:9" -> DpSize(width = (base * 1.4f).dp, height = (base * 0.78f).dp)
    "1:1" -> DpSize(width = base.dp, height = base.dp)
    else -> DpSize(width = base.dp, height = base.dp)
  }
}
