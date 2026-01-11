package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.presentation.components.LargeIconButton
import com.ruhaan.orangeeditor.presentation.theme.Typography

@Composable
fun EditorTopBar(
    modifier: Modifier = Modifier,
    fileName: String,
    canUndo: Boolean,
    canRedo: Boolean,
    canDelete: Boolean,
    onBackClick: () -> Unit,
    onFileNameClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDraftClick : () -> Unit,
    onExportClick: () -> Unit,
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

    Row(
        modifier = Modifier.weight(weight = 3f).clickable { onFileNameClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
      Text(
          modifier = Modifier.weight(1f, fill = false),
          text = fileName,
          style = Typography.titleLarge.copy(fontWeight = FontWeight.Medium),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          textDecoration = TextDecoration.Underline,
      )
      Icon(
          modifier = Modifier.padding(start = 8.dp),
          painter = painterResource(R.drawable.ic_edit_pencil),
          contentDescription = null,
      )
    }

    LargeIconButton(
        modifier = Modifier.weight(1f),
        iconId = R.drawable.ic_undo_arrow,
        contentDescription = "undo changes",
        enabled = canUndo,
    ) {
      onUndoClick()
    }

    LargeIconButton(
        modifier = Modifier.weight(1f),
        iconId = R.drawable.ic_redo_arrow,
        contentDescription = "redo changes",
        enabled = canRedo,
    ) {
      onRedoClick()
    }

    LargeIconButton(
        modifier = Modifier.weight(1f),
        iconId = R.drawable.ic_delete,
        contentDescription = "delete layer",
        enabled = canDelete,
    ) {
      onDeleteClick()
    }
    LargeIconButton(
        modifier = Modifier.weight(1f),
        iconId = R.drawable.ic_text,
        contentDescription = "Draft",
    ) {
      onDraftClick()
    }

    Button(
        onClick = onExportClick,
        colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White),
    ) {
      Text(text = "Export", style = Typography.titleSmall)
    }
  }
}
