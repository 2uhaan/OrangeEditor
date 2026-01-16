package com.ruhaan.orangeeditor.domain.model.layer

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

data class TextLayer(
    override val id: String,
    val displayName: String,
    val text: String,
    val color: Color,
    /** This only support only Normal and Bold. */
    val fontWeight: FontWeight,
    /** This only support only Normal and Italic. */
    val fontStyle: FontStyle,
    override val bitmap: Bitmap?,
    override val transform: Transform,
    override val zIndex: Int,
    override val visible: Boolean = true,
) : Layer(id = id, transform = transform, zIndex = zIndex, visible = visible, bitmap = bitmap)
