package com.ruhaan.orangeeditor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruhaan.orangeeditor.data.entity.EditorStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
// table name editor_states
interface EditorStateDao {

  @Query("SELECT * FROM editor_states") fun getAllEditorStateEntity(): Flow<List<EditorStateEntity>>

  @Query("SELECT * FROM EDITOR_STATES WHERE editorId = :editorId")
  suspend fun getEditorStateEntityById(editorId: String): EditorStateEntity

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveEditorStateEntity(editorStateEntity: EditorStateEntity)

  @Query("DELETE FROM editor_states WHERE editorId = :editorId")
  suspend fun deleteEditorStateEntity(editorId: String)
}
