package com.ruhaan.orangeeditor.domain.model.layer

import android.graphics.Bitmap

data class EmojiLayer(
    override val id: String,
    val bitmap: Bitmap,
    override val transform: Transform,
    override val zIndex: Int,
    override val visible: Boolean = true,
) : Layer(id = id, transform = transform, zIndex = zIndex, visible = visible)
