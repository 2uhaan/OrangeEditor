package com.ruhaan.orangeeditor.data.mapper

import com.ruhaan.orangeeditor.data.entity.EditorStateEntity
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.Layer

fun EditorStateEntity.toDomain(layers: List<Layer>): EditorState {
  return EditorState(
      editorId = editorId,
      layers = layers,
      canvasFormat = canvasFormat,
      selectedLayerId = selectedLayerId,
      fileName = fileName,
  )
}

fun EditorState.toEntity(): EditorStateEntity {
  return EditorStateEntity(
      editorId = editorId,
      canvasFormat = canvasFormat,
      selectedLayerId = selectedLayerId,
      fileName = fileName,
  )
}
