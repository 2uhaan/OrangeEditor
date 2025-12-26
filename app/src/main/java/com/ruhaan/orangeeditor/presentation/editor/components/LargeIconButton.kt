package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.presentation.theme.Typography

@Composable
fun LargeIconButton(
    modifier: Modifier = Modifier,
    iconId: Int,
    contentDescription: String,
    label: String? = null,
    onClick: () -> Unit,
) {
  Box(
      modifier = modifier.clip(CircleShape).size(size = 52.dp).clickable(onClick = onClick),
      contentAlignment = Alignment.Center,
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
          painter = painterResource(id = iconId),
          contentDescription = contentDescription,
          modifier = Modifier.size(size = 28.dp),
      )
      label?.let { Text(text = label, style = Typography.labelSmall.copy(color = Color.Gray)) }
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLargeIconButton() {
  LargeIconButton(
      iconId = R.drawable.ic_import_image,
      contentDescription = "import image",
      label = "Import",
  ) {}
}
