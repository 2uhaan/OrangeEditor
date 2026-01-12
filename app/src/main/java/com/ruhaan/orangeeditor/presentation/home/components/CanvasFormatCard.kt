package com.ruhaan.orangeeditor.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.presentation.theme.CardBackground
import com.ruhaan.orangeeditor.presentation.theme.TextPrimary

@Composable
fun CanvasFormatCard(
    modifier: Modifier = Modifier,
    canvasFormat: CanvasFormat,
    onClick: () -> Unit,
) {
  Card(
      modifier = modifier.size(180.dp).clickable { onClick() },
      colors = CardDefaults.cardColors(containerColor = CardBackground),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
      AspectRatioPreview(aspectRatio = canvasFormat.aspectRatio)

      Spacer(modifier = Modifier.height(12.dp))

      Text(
          text = canvasFormat.title,
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimary,
      )
    }
  }
}
