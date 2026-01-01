package com.ruhaan.orangeeditor.presentation.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.presentation.editor.components.AddTextSheet
import com.ruhaan.orangeeditor.presentation.editor.components.EditorBottomBar
import com.ruhaan.orangeeditor.presentation.editor.components.EditorCanvas
import com.ruhaan.orangeeditor.presentation.editor.components.EditorTopBar
import com.ruhaan.orangeeditor.presentation.editor.components.FilterRow
import com.ruhaan.orangeeditor.presentation.editor.components.SelectedLayerGestureLayer
import com.ruhaan.orangeeditor.presentation.navigation.Route
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel,
    canvasFormat: CanvasFormat,
    navController: NavHostController,
) {
  // ViewModel states
  val state by viewModel.state.collectAsState()

  // Local states
  var canvasSize by remember { mutableStateOf(IntSize.Zero) }
  var showAddTextSheet by remember { mutableStateOf(false) }
  var showImageFilters by remember { mutableStateOf(false) }

  Scaffold(
      topBar = {
        Box(modifier = Modifier.background(color = CanvasOrange)) {
          EditorTopBar(
              modifier = Modifier.fillMaxWidth().statusBarsPadding(),
              onBackClick = { navController.popBackStack() },
          ) {}
        }
      },
      bottomBar = {
        Column(modifier = Modifier.background(Color.White)) {
          if (showImageFilters) {
            FilterRow(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                onClick = { imageFilter ->
                  viewModel.updateImageFilterOfSelectedImagerLayer(imageFilter)
                },
            )
          }
          HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
          Spacer(modifier = Modifier.height(height = 4.dp))
          EditorBottomBar(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).navigationBarsPadding(),
              onImageImportClick = { loadedBitmap ->
                viewModel.addImageLayer(
                    bitmap = loadedBitmap,
                    canvasWidthInPx = canvasSize.width.toFloat(),
                    canvasHeightInPx = canvasSize.height.toFloat(),
                )
              },
              onTextClick = { showAddTextSheet = true },
              onFilterClick = { showImageFilters = !showImageFilters },
              onCropClick = { navController.navigate(Route.CropScreen.route) },
          )
        }
      },
  ) { innerPadding ->
    if (showAddTextSheet)
        AddTextSheet(
            onDismissRequest = { showAddTextSheet = false },
            onTextAdd = { text, fontSize, fontColor, fontWeight, fontStyle ->
              viewModel.addTextLayer(
                  text = text,
                  fontSizeInPx = fontSize,
                  color = fontColor,
                  fontWeight = fontWeight,
                  fontStyle = fontStyle,
                  canvasWidthInPx = canvasSize.width.toFloat(),
                  canvasHeightInPx = canvasSize.height.toFloat(),
              )
            },
        )

    Box(
        modifier =
            modifier.fillMaxSize().padding(innerPadding).background(color = Color(0xFFfaf9f6)),
        contentAlignment = Alignment.Center,
    ) {
      EditorCanvas(
          state = state,
          canvasFormat = canvasFormat,
          onCanvasSize = { size -> canvasSize = size },
      )
      SelectedLayerGestureLayer(
          state = state,
          onUpdateLayer = viewModel::updateLayer,
      )
    }
  }
}
