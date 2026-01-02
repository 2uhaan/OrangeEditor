package com.ruhaan.orangeeditor.domain.model.layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

data class TextLayer(
    override val id: String,
    val text: String,
    val color: Color,
    val fontSizeInPx: Float,
    val fontFamily: FontFamily,
    override val transform: Transform,
    override val zIndex: Int,
    override val visible: Boolean = true,
) : Layer(id = id, transform = transform, zIndex = zIndex, visible = visible)
