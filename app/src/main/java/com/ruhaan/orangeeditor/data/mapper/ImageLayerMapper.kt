package com.ruhaan.orangeeditor.data.mapper

import android.graphics.Bitmap
import com.ruhaan.orangeeditor.data.entity.ImageLayerEntity
import com.ruhaan.orangeeditor.data.entity.LayerBase
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer

fun ImageLayerEntity.toDomain(bitmap: Bitmap?): ImageLayer {
  return ImageLayer(
      id = id,
      displayName = displayName,
      bitmap = bitmap,
      imageFilter = imageFilter,
      adjustment = adjustment,
      originalWidth = originalWidth,
      originalHeight = originalHeight,
      transform = base.transform,
      zIndex = base.zIndex,
      visible = base.visible,
  )
}

fun ImageLayer.toEntity(editorId: String, bitmapPath: String): ImageLayerEntity {
  return ImageLayerEntity(
      id = id,
      base = LayerBase(transform, zIndex, visible, bitmapPath),
      adjustment = adjustment,
      editorId = editorId,
      imageFilter = imageFilter,
      originalWidth = originalWidth,
      originalHeight = originalHeight,
      displayName = displayName,
  )
}
