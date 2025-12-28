package com.ruhaan.orangeeditor.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ruhaan.orangeeditor.domain.model.CanvasFormat
import com.ruhaan.orangeeditor.presentation.home.components.CanvasGrid
import com.ruhaan.orangeeditor.presentation.home.components.HomeHeader
import com.ruhaan.orangeeditor.presentation.navigation.Route
import com.ruhaan.orangeeditor.presentation.theme.BackgroundLight

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
  Column(modifier = modifier.fillMaxSize().background(BackgroundLight).padding(top = 60.dp)) {
    CanvasGrid(
        canvasFormats = CanvasFormat.allFormats,
        onFormatClick = { selectedFormat ->
          navController.navigate(Route.Editor.createRoute(selectedFormat))
        },
        headerContent = { HomeHeader() },
    )
  }
}
