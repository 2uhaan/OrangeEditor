package com.ruhaan.orangeeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.ruhaan.orangeeditor.presentation.navigation.NavGraph
import com.ruhaan.orangeeditor.presentation.theme.OrangeEditorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { OrangeEditorTheme { OrangeEditorApp() } }
  }
}

@Composable
fun OrangeEditorApp() {
  val navController = rememberNavController()
  NavGraph(navController = navController)
}
