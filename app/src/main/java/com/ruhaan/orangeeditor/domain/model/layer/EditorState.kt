package com.ruhaan.orangeeditor.domain.model.layer

import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import java.util.UUID

data class EditorState(
    val editorId: String = UUID.randomUUID().toString(),
    val canvasFormat: CanvasFormat = CanvasFormat.POST,
    val layers: List<Layer> = emptyList(),
    val selectedLayerId: String? = null,
    val fileName: String = "Draft",
)
