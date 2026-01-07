package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange
import com.ruhaan.orangeeditor.presentation.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayerPositionSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    layers: List<Layer>,
    selectedLayerId: String?,
    onLayerSelected: (String) -> Unit,
    onMoveUp: (String) -> Unit,
    onMoveDown: (String) -> Unit,
) {

  ModalBottomSheet(
      modifier = modifier,
      onDismissRequest = onDismissRequest,
  ) {
    Column(
        modifier = Modifier.padding(top = 8.dp, end = 16.dp, bottom = 16.dp, start = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // Header
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            text = "Layers",
            style = Typography.titleMedium,
        )
        Button(
            onClick = onDismissRequest,
        ) {
          Text(text = "Done")
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Layer list
      LazyColumn(
          modifier = Modifier.fillMaxWidth().weight(1f),
          verticalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        items(layers.reversed()) { layer ->
          LayerPositionItem(
              layer = layer,
              isSelected = layer.id == selectedLayerId,
              onLayerLongPress = { onLayerSelected(layer.id) },
              onMoveUp = { onMoveUp(layer.id) },
              onMoveDown = { onMoveDown(layer.id) },
          )
        }
      }
    }
  }
}

@Composable
private fun LayerPositionItem(
    layer: Layer,
    isSelected: Boolean,
    onLayerLongPress: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
  val shape = RoundedCornerShape(8.dp)

  // Generate display name: Text_1, Image_1, etc.
  val layerName =
      when {
        layer.id.startsWith("text_") -> "Text_${layer.id.substringAfter("text_")}"
        layer.id.startsWith("image_") -> "Image_${layer.id.substringAfter("image_")}"
        else -> layer.id
      }

  Row(
      modifier =
          Modifier.fillMaxWidth()
              .clip(shape)
              .border(
                  border =
                      if (isSelected) BorderStroke(2.dp, CanvasOrange)
                      else BorderStroke(1.dp, Color.LightGray),
                  shape = shape,
              )
              .combinedClickable(
                  onClick = { /* Click does nothing */ },
                  onLongClick = onLayerLongPress,
              )
              .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(
        text = layerName,
        style = Typography.bodyMedium,
    )

    Row {
      IconButton(
          onClick = onMoveUp,
          enabled = layer.zIndex > 0,
      ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_up),
            contentDescription = "Move up",
            tint = LocalContentColor.current,
        )
      }
      IconButton(
          onClick = onMoveDown,
          enabled = layer.zIndex < Int.MAX_VALUE,
      ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_down),
            contentDescription = "Move down",
            tint = LocalContentColor.current,
        )
      }
    }
  }
}
