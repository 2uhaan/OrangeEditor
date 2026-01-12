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
import com.ruhaan.orangeeditor.Constant.MAX_DIMENSION
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.presentation.components.LargeIconButton
import com.ruhaan.orangeeditor.util.BottomBarMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditorBottomBar(
    modifier: Modifier = Modifier,
    mode: BottomBarMode,
    onImageImportClick: (Bitmap) -> Unit,
    onAddTextClick: () -> Unit,
    onEditTextClick: () -> Unit,
    onFilterClick: () -> Unit,
    onAdjustmentsClick: () -> Unit,
    onCropClick: () -> Unit,
    onPositionClick: () -> Unit,
    onExportClick: () -> Unit,
    onImageLoading: (Boolean) -> Unit,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val launcher =
      rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri
        ->
        if (uri != null) {
          scope.launch {
            onImageLoading(true)
            val bitmap = loadBitmapFromUri(context, uri, onImageLoading)
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
          contentDescription = "Add text",
          label = "Add Text",
      ) {
        onAddTextClick()
      }
    }

    if (mode == BottomBarMode.TextLayerSelected) {
      item {
        LargeIconButton(
            modifier = Modifier.widthIn(min = 80.dp),
            iconId = R.drawable.ic_edit_text,
            contentDescription = "Edit text",
            label = "Edit Text",
        ) {
          onEditTextClick()
        }
      }
    }

    if (mode == BottomBarMode.ImageLayerSelected) {
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
    }

    if (mode == BottomBarMode.ImageLayerSelected) {
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
    }

    if (mode == BottomBarMode.ImageLayerSelected) {
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

    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_position,
          contentDescription = "Position",
          label = "Position",
      ) {
        onPositionClick()
      }
    }
    item {
      LargeIconButton(
          modifier = Modifier.widthIn(min = 80.dp),
          iconId = R.drawable.ic_export,
          contentDescription = "export image",
          label = "Export",
      ) {
        onExportClick()
      }
    }
  }
}

suspend fun loadBitmapFromUri(
  context: Context,
  uri: Uri,
  onImageLoading: (Boolean) -> Unit,
): Bitmap? =
  withContext(Dispatchers.IO) {
    onImageLoading(true)
    try {
      val source = ImageDecoder.createSource(context.contentResolver, uri)

      ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
        decoder.isMutableRequired = true
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE

        val srcWidth = info.size.width
        val srcHeight = info.size.height

        // ---- HARD LIMIT (no upscaling) ----
        val scale =
          minOf(
            MAX_DIMENSION.toFloat() / srcWidth,
            MAX_DIMENSION.toFloat() / srcHeight,
            1f
          )

        val targetWidth = (srcWidth * scale).toInt()
        val targetHeight = (srcHeight * scale).toInt()

        if (targetWidth != srcWidth || targetHeight != srcHeight) {
          decoder.setTargetSize(targetWidth, targetHeight)
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      null
    } finally {
      onImageLoading(false)
    }
  }

/*

Primary -> (default: Add Image, Add Text, Layer Position, Export)

TextLayerSelected -> (Add Image, Add Text, Layer Position, Export)

ImageLayerSelected -> (Add Image, Add Text, Filter, Adjustments, Crop, Layer Position, Export)

 */
