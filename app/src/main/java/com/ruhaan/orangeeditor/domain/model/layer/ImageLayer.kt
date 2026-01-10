package com.ruhaan.orangeeditor.domain.model.layer

import android.graphics.Bitmap

data class ImageLayer(
    override val id: String,
    val bitmap: Bitmap?,
    val imageFilter: ImageFilter,
    val adjustment: Adjustment,
    val originalWidth: Int,
    val originalHeight: Int,
    override val transform: Transform,
    override val zIndex: Int,
    override val visible: Boolean = true,
) : Layer(id = id, transform = transform, zIndex = zIndex, visible = visible)
