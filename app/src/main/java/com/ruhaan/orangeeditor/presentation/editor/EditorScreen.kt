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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.presentation.editor.components.CanvasArea
import com.ruhaan.orangeeditor.presentation.editor.components.EditorBottomBar
import com.ruhaan.orangeeditor.presentation.editor.components.EditorTopBar
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun EditorScreen(modifier: Modifier = Modifier) {
  Scaffold(
      topBar = {
        Box(modifier = Modifier.background(color = CanvasOrange)) {
          EditorTopBar(modifier = Modifier.fillMaxWidth().statusBarsPadding())
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
  ) { _ ->
    Box(
        modifier = modifier.fillMaxSize().background(color = Color(0xFFfaf9f6)),
        contentAlignment = Alignment.Center,
    ) {
      CanvasArea()
    }
  }
}
