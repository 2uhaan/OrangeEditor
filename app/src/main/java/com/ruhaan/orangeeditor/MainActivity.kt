package com.ruhaan.orangeeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.ruhaan.orangeeditor.presentation.editor.EditorViewModel
import com.ruhaan.orangeeditor.presentation.navigation.NavGraph
import com.ruhaan.orangeeditor.presentation.theme.OrangeEditorTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val viewmodel: EditorViewModel by viewModels()
    setContent { OrangeEditorTheme { OrangeEditorApp(viewmodel = viewmodel) } }
  }
}

@Composable
fun OrangeEditorApp(viewmodel: EditorViewModel) {
  val navController = rememberNavController()
  NavGraph(navController = navController, viewmodel = viewmodel)
}
