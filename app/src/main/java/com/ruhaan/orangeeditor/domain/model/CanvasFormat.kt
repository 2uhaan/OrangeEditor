package com.ruhaan.orangeeditor.domain.model


// Data class representing each canvas format option
data class CanvasFormat(
    val title: String,        // ex: (Story, Post, etc.)
    val aspectRatio: String,  // Ratio text to show on preview (9:16, 1:1, etc.)
    val width: Int,
    val height: Int
)

// Static list of all available canvas formats (Abhi Limited hai for sample)
object CanvasFormats {
    val allFormats = listOf(
        CanvasFormat(
            title = "Story",
            aspectRatio = "9:16",
            width = 1080,
            height = 1920
        ),
        CanvasFormat(
            title = "Post",
            aspectRatio = "1:1",
            width = 1080,
            height = 1080
        ),
        CanvasFormat(
            title = "Portrait",
            aspectRatio = "4:5",
            width = 1080,
            height = 1350
        ),
        CanvasFormat(
            title = "Thumbnail",
            aspectRatio = "16:9",
            width = 1920,
            height = 1080
        )
    )
}
