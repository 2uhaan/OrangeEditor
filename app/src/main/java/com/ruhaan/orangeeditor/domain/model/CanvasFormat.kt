package com.ruhaan.orangeeditor.domain.model

enum class CanvasFormat(
    val title: String,        // Display name
    val aspectRatio: String,  // Ratio text for UI
    val width: Int,           // Canvas width in pixels
    val height: Int           // Canvas height in pixels
) {
    STORY(
        title = "Story",
        aspectRatio = "9:16",
        width = 1080,
        height = 1920
    ),
    POST(
        title = "Post",
        aspectRatio = "1:1",
        width = 1080,
        height = 1080
    ),
    PORTRAIT(
        title = "Portrait",
        aspectRatio = "4:5",
        width = 1080,
        height = 1350
    ),
    THUMBNAIL(
        title = "Thumbnail",
        aspectRatio = "16:9",
        width = 1920,
        height = 1080
    );
    // Companion object for accessing all formats as a list
    companion object {
        val allFormats: List<CanvasFormat> = entries
    }
}
