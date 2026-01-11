package com.ruhaan.orangeeditor.domain.repository

import com.ruhaan.orangeeditor.data.entity.EditorStateEntity
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import kotlinx.coroutines.flow.Flow

interface OrangeRepository {

  fun getAllEditorState(): Flow<List<EditorStateEntity>>

  suspend fun getEditorStatById(editorId: String): EditorState

  suspend fun saveEditorState(editorState: EditorState)

  suspend fun deleteEditorStateById(editorId: String)
}
