package com.ruhaan.orangeeditor.domain.model.layer

data class EditorState(
    val layers: List<Layer> = emptyList(),
    val selectedLayerId: String? = null,
    val fileName: String = "Draft",
)
