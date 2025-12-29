package com.ruhaan.orangeeditor.presentation.navigation

import com.ruhaan.orangeeditor.domain.CanvasFormat

sealed class Route(val route: String) {

    data object Home : Route("home")

    data object Editor : Route("editor/{canvasFormat}") {
        // Argument key used for navigation and retrieval
        const val ARG_CANVAS_FORMAT = "canvasFormat"

        // Helper function to build the complete route with the selected format
        fun createRoute(format: CanvasFormat): String {
            return "editor/${format.name}" // Uses enum's name
        }
    }
}
