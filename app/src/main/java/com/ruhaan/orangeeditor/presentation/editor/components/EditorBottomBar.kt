package com.ruhaan.orangeeditor.presentation.editor.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.presentation.components.LargeIconButton

@Composable
fun EditorBottomBar(
    modifier: Modifier = Modifier,
    onImageImportClick: (Bitmap) -> Unit = {},
    onTextAdd:
        (
            text: String,
            fontSize: Int,
            fontColor: Color,
            fontWeight: FontWeight,
            fontStyle: FontStyle,
        ) -> Unit,
    onFilterClick: () -> Unit = {},
    onAdjustmentsClick: () -> Unit = {},
    onCropClick: () -> Unit = {},
) {
  val context = LocalContext.current
  var showAddTextSheet by remember { mutableStateOf(false) }

  val launcher =
      rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri
        ->
        if (uri != null) {
          val bitmap = loadBitmapFromUri(context, uri)
          bitmap?.let { onImageImportClick(it) }
        }
      }

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
        launcher.launch(
            input =
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
        )
      }
    }
    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_text,
          contentDescription = "text",
          label = "Text",
      ) {
        showAddTextSheet = true
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_image_filter,
          contentDescription = "filters",
          label = "Filter",
      ) {
        // TODO: Add filter functionality
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_adjustments,
          contentDescription = "adjustments",
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
          onClick = onCropClick,
      )
    }
  }

  if (showAddTextSheet)
      AddTextSheet(onDismissRequest = { showAddTextSheet = false }, onTextAdd = onTextAdd)
}

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
  return try {
    val source = ImageDecoder.createSource(context.contentResolver, uri)
    ImageDecoder.decodeBitmap(source)
  } catch (e: Exception) {
    e.printStackTrace()
    null
  }
}
