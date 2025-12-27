package com.ruhaan.orangeeditor

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ruhaan.orangeeditor.domain.model.CanvasFormat
import com.ruhaan.orangeeditor.presentation.editor.EditorScreen
import com.ruhaan.orangeeditor.presentation.home.HomeScreen
import com.ruhaan.orangeeditor.presentation.navigation.Route


@Composable
fun OrangeEditorApp() {

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Home.route  // App starts at HomeScreen
    ) {
        composable(route = Route.Home.route) {
            HomeScreen(
                onFormatSelected = { selectedFormat ->
                    navController.navigate(Route.Editor.createRoute(selectedFormat))
                }
            )
        }
        composable(
            route = Route.Editor.route,
            arguments = listOf(
                navArgument(Route.Editor.ARG_CANVAS_FORMAT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val formatName = backStackEntry.arguments?.getString(Route.Editor.ARG_CANVAS_FORMAT)

            // Convert String back to CanvasFormat enum
            val selectedFormat = formatName?.let {
                CanvasFormat.valueOf(it)
            } ?: CanvasFormat.POST

            EditorScreen(
                canvasFormat = selectedFormat,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
