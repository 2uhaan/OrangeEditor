package com.ruhaan.orangeeditor.presentation.navigation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import com.ruhaan.orangeeditor.presentation.editor.EditorScreen
import com.ruhaan.orangeeditor.presentation.home.HomeScreen
import kotlinx.coroutines.delay

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Route.Home.route) {
  NavHost(navController = navController, startDestination = startDestination) {
    composable(route = Route.Home.route) { HomeScreen(navController = navController) }

    composable(
        route = Route.Editor.route,
        arguments =
            listOf(navArgument(Route.Editor.ARG_CANVAS_FORMAT) { type = NavType.StringType }),
    ) { backStackEntry ->
      val formatName = backStackEntry.arguments?.getString(Route.Editor.ARG_CANVAS_FORMAT)
      val selectedFormat = formatName?.let { CanvasFormat.valueOf(it) } ?: CanvasFormat.POST

      val context = LocalContext.current
      var backPressedOnce by remember { mutableStateOf(false) }

      LaunchedEffect(key1 = backPressedOnce) {
        if (backPressedOnce) {
          delay(2000)
          backPressedOnce = false
        }
      }

      BackHandler(enabled = true) {
        if (backPressedOnce) {
          navController.popBackStack()
        } else {
          backPressedOnce = true
          Toast.makeText(
                  context,
                  "Press back again to exit and discard changes",
                  Toast.LENGTH_SHORT,
              )
              .show()
        }
      }

      EditorScreen(canvasFormat = selectedFormat, navController = navController)
    }
  }
}
