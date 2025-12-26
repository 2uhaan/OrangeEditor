package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.R

@Composable
@Preview(showBackground = true)
fun EditorBottomBar(modifier: Modifier = Modifier) {
  LazyRow(
      modifier = modifier,
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_import_image,
          contentDescription = "import image",
          label = "Import",
      ) {
        // TODO: Add import functionality
      }
    }
    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_text,
          contentDescription = "import image",
          label = "Text",
      ) {
        // TODO: Add import functionality
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_emoji,
          contentDescription = "add emoji",
          label = "Emoji",
      ) {
        // TODO: Add emoji functionality
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_image_filter,
          contentDescription = "apply filter",
          label = "Filter",
      ) {
        // TODO: Add filter functionality
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_adjustments,
          contentDescription = "apply adjustments",
          label = "Adjust",
      ) {
        // TODO: Add adjustments functionality
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_crop,
          contentDescription = "crop image",
          label = "Crop",
      ) {
        // TODO: Add crop functionality
      }
    }
  }
}
