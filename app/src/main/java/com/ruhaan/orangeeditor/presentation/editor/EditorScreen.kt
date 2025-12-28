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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ruhaan.orangeeditor.domain.model.CanvasFormat
import com.ruhaan.orangeeditor.presentation.editor.components.CanvasArea
import com.ruhaan.orangeeditor.presentation.editor.components.EditorBottomBar
import com.ruhaan.orangeeditor.presentation.editor.components.EditorTopBar
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    canvasFormat: CanvasFormat,
    navController: NavHostController,
) {

  Scaffold(
      topBar = {
        Box(modifier = Modifier.background(color = CanvasOrange)) {
          EditorTopBar(
              modifier = Modifier.fillMaxWidth().statusBarsPadding(),
              onBackClick = { navController.popBackStack() },
          )
        }
      },
      bottomBar = {
        Column {
          HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
          Spacer(modifier = Modifier.height(height = 4.dp))
          EditorBottomBar(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).navigationBarsPadding()
          )
        }
      },
  ) { paddingValues ->
    Box(
        modifier =
            modifier.fillMaxSize().padding(paddingValues).background(color = Color(0xFFfaf9f6)),
        contentAlignment = Alignment.Center,
    ) {
      CanvasArea(canvasFormat = canvasFormat)
    }
  }
}
