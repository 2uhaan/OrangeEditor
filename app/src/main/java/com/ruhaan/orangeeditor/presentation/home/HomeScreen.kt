package com.ruhaan.orangeeditor.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.presentation.editor.EditorViewModel
import com.ruhaan.orangeeditor.presentation.home.components.CanvasGrid
import com.ruhaan.orangeeditor.presentation.navigation.Route
import com.ruhaan.orangeeditor.presentation.theme.BackgroundLight
import com.ruhaan.orangeeditor.presentation.theme.TextPrimary
import com.ruhaan.orangeeditor.presentation.theme.TextSecondary

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewmodel: EditorViewModel,
    navController: NavHostController,
) {
  Column(modifier = modifier.fillMaxSize().background(BackgroundLight).padding(top = 60.dp)) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
      Spacer(modifier = Modifier.height(25.dp))
      Text(
          text = "Orange Editor",
          fontSize = 36.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary,
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(text = "What will you design today?", fontSize = 16.sp, color = TextSecondary)

      Spacer(modifier = Modifier.height(40.dp))

      Text(
          text = "Create New",
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimary,
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    CanvasGrid(
        canvasFormats = CanvasFormat.entries,
        onFormatClick = { selectedFormat ->
          viewmodel.newEditorState(canvasFormat = selectedFormat)
          navController.navigate(Route.Editor.route)
        },
    )
  }
}
