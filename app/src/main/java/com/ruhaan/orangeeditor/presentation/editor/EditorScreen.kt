package com.ruhaan.orangeeditor.presentation.editor

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.CanvasFormat
import com.ruhaan.orangeeditor.presentation.editor.components.CanvasArea
import com.ruhaan.orangeeditor.presentation.editor.components.EditorBottomBar
import com.ruhaan.orangeeditor.presentation.editor.components.EditorTopBar
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    canvasFormat: CanvasFormat,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current

    // Double-press back handling state
    var backPressedOnce by remember { mutableStateOf(false) }

    // Reset backPressedOnce after 2 seconds
    LaunchedEffect(key1 = backPressedOnce) {
        if (backPressedOnce) {
            delay(2000)  // 2 second window to press back again
            backPressedOnce = false
        }
    }

    // Intercept system back button (ONLY for system back, not TopBar back button)
    BackHandler(enabled = true) {
        if (backPressedOnce) {
            // Second press - actually navigate back
            onNavigateBack()
        } else {
            // First press - show warning toast
            backPressedOnce = true
            Toast.makeText(
                context,
                "Press back again to exit",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

  Scaffold(
      topBar = {
        Box(modifier = Modifier.background(color = CanvasOrange)) {
          EditorTopBar(
              modifier = Modifier
                  .fillMaxWidth()
                  .statusBarsPadding(),
              onBackClick = onNavigateBack,
          )
        }
      },
      bottomBar = {
        Column {
          HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
          Spacer(modifier = Modifier.height(height = 4.dp))
          EditorBottomBar(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .navigationBarsPadding()
          )
        }
      },
  ) { paddingValues ->
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)  // Applied scaffold padding to avoid toolbar overlap
            .background(color = Color(0xFFfaf9f6)),
        contentAlignment = Alignment.Center,
    ) {
      CanvasArea(canvasFormat = canvasFormat)
    }
  }
}
