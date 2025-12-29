package com.ruhaan.orangeeditor.domain

enum class CanvasFormat(
    val title: String,
    val aspectRatio: String,
    val width: Int,
    val height: Int,
) {
  STORY(title = "Story", aspectRatio = "9:16", width = 1080, height = 1920),
  POST(title = "Post", aspectRatio = "1:1", width = 1080, height = 1080),
  PORTRAIT(title = "Portrait", aspectRatio = "4:5", width = 1080, height = 1350),
  THUMBNAIL(title = "Thumbnail", aspectRatio = "16:9", width = 1920, height = 1080),
}
