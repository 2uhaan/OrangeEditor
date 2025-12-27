package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.presentation.theme.Typography

@Composable
fun EditorTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
  Row(
      modifier = modifier.padding(end = 15.dp, bottom = 5.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    LargeIconButton(
        modifier = Modifier.weight(1f),
        iconId = R.drawable.ic_back_arrow,
        contentDescription = "go back",
    ) {
        onBackClick()
    }

    Text(
        text = "Draft",
        modifier = Modifier.weight(weight = 3f),
        style = Typography.titleLarge.copy(fontWeight = FontWeight.Medium),
    )

    LargeIconButton(
        modifier = Modifier.weight(1f),
        iconId = R.drawable.ic_undo_arrow,
        contentDescription = "undo changes",
    ) {
      // TODO : Add undo functionality
    }

    LargeIconButton(
        modifier = Modifier.weight(1f),
        iconId = R.drawable.ic_redo_arrow,
        contentDescription = "redo changes",
    ) {
      // TODO : Add redo functionality
    }

    Button(
        onClick = {
          // TODO : Add export functionality
        },
        colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White),
    ) {
      Text(text = "Export", style = Typography.titleSmall)
    }
  }
}
