package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.presentation.theme.Typography

@Composable
@Preview
fun RadioButton(
    modifier: Modifier = Modifier,
    text: String = "Hello",
    style: TextStyle = Typography.bodyMedium,
    onClick: () -> Unit = {},
) {
  Button(
      modifier = modifier,
      onClick = onClick,
      shape = RoundedCornerShape(8.dp),
      contentPadding = PaddingValues(horizontal = 12.dp),
      colors = ButtonDefaults.buttonColors(containerColor = Color.White),
  ) {
    Text(text = text, style = style)
  }
}
