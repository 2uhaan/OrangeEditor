package com.ruhaan.orangeeditor.data.entity

import androidx.room.Embedded
import com.ruhaan.orangeeditor.domain.model.layer.Transform

data class LayerBase(
    @Embedded(prefix = "transform_") val transform: Transform,
    val zIndex: Int,
    val visible: Boolean = true,
    val bitmapStoredPath: String,
)
