package com.ruhaan.orangeeditor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruhaan.orangeeditor.data.entity.TextLayerEntity

@Dao
// table name text_layers
interface TextLayerDao {

  @Query("SELECT * FROM text_layers WHERE editorId = :editorId")
  suspend fun geTextLayerByEditorStateId(editorId: String): List<TextLayerEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveTextLayerEntity(textLayerEntity: TextLayerEntity)
}
