package com.ruhaan.orangeeditor.presentation.crop

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.Layer
import com.ruhaan.orangeeditor.presentation.crop.components.ActionBar
import com.tanishranjan.cropkit.CropController
import com.tanishranjan.cropkit.CropDefaults
import com.tanishranjan.cropkit.ImageCropper
import com.tanishranjan.cropkit.rememberCropController

@Composable
fun CropScreen(
    modifier: Modifier = Modifier,
    selectedLayer: Layer?,
    onSave: (Bitmap) -> Unit,
    onNavigateBack: () -> Unit,
) {
  var cropController: CropController?

  if (selectedLayer is ImageLayer) {
    cropController =
        rememberCropController(
            bitmap = selectedLayer.bitmap,
            cropOptions = CropDefaults.cropOptions(handleRadius = 12.dp, touchPadding = 14.dp),
        )
    Scaffold { innerPadding ->
      Column(
          modifier = modifier.fillMaxSize().padding(paddingValues = innerPadding),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        ImageCropper(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(24.dp),
            cropController = cropController,
        )
        ActionBar(
            onVerticalFlip = cropController::flipVertically,
            onHorizontalFlip = cropController::flipHorizontally,
            onAntiClockWiseRotate = cropController::rotateAntiClockwise,
            onClockWiseRotate = cropController::rotateClockwise,
        )
        Button(
            onClick = {
              onSave(cropController.crop())
              onNavigateBack()
            }
        ) {
          Text("Save")
        }
      }
    }
  } else {
    onNavigateBack()
  }
}
