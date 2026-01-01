package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.layer.ImageFilter
import com.ruhaan.orangeeditor.presentation.theme.Typography

@Composable
fun FilterRow(modifier: Modifier = Modifier, onClick: (ImageFilter) -> Unit) {
  val imageFilter = ImageFilter.entries
  LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
    items(imageFilter) { Filter(imageFilter = it, onClick = onClick) }
  }
}

@Composable
fun Filter(imageFilter: ImageFilter, onClick: (ImageFilter) -> Unit) {
  Column(
      modifier = Modifier.clickable(onClick = { onClick(imageFilter) }).width(width = 75.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
        painter = painterResource(imageFilter.previewImageId),
        contentDescription = imageFilter.previewName,
        contentScale = ContentScale.FillWidth,
    )
    Text(
        text = imageFilter.previewName,
        style = Typography.labelLarge,
        textAlign = TextAlign.Center,
    )
  }
}
