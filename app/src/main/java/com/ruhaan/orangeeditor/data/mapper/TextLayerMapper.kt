package com.ruhaan.orangeeditor.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.ruhaan.orangeeditor.data.entity.LayerBase
import com.ruhaan.orangeeditor.data.entity.TextLayerEntity
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer

fun TextLayerEntity.toDomain(): TextLayer {
  return TextLayer(
      id = id,
      text = text,
      color = Color(colorArgb),
      fontSizeInPx = fontSizeInPx,
      fontWeight = if (fontWeightValue == 0) FontWeight.Normal else FontWeight.Bold,
      fontStyle = if (fontStyleValue == 0) FontStyle.Normal else FontStyle.Italic,
      transform = base.transform,
      zIndex = base.zIndex,
      visible = base.visible,
  )
}

fun TextLayer.toEntity(editorId: String): TextLayerEntity {
  return TextLayerEntity(
      id = id,
      base = LayerBase(transform, zIndex, visible),
      editorId = editorId,
      text = text,
      colorArgb = color.toArgb(),
      fontSizeInPx = fontSizeInPx,
      fontWeightValue = if (fontWeight == FontWeight.Normal) 0 else 1,
      fontStyleValue = if (fontStyle == FontStyle.Normal) 0 else 1,
  )
}
