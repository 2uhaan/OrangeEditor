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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.presentation.components.LargeIconButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditorBottomBar(
    modifier: Modifier = Modifier,
    onImageImportClick: (Bitmap) -> Unit,
    onTextClick: () -> Unit,
    onFilterClick: () -> Unit,
    onAdjustmentsClick: () -> Unit,
    onCropClick: () -> Unit,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val launcher =
      rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri
        ->
        if (uri != null) {
          scope.launch {
            val bitmap = loadBitmapFromUri(context, uri)
            bitmap?.let { onImageImportClick(it) }
          }
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
        onTextClick()
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_image_filter,
          contentDescription = "filters",
          label = "Filter",
      ) {
        onFilterClick()
      }
    }

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_adjustments,
          contentDescription = "adjustments",
          label = "Adjust",
      ) {
        onAdjustmentsClick()
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
}

suspend fun loadBitmapFromUri(
    context: Context,
    uri: Uri,
): Bitmap? =
    withContext(Dispatchers.IO) {
      try {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
          decoder.isMutableRequired = true
          decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
      } catch (e: Exception) {
        e.printStackTrace()
        null
      }
    }
