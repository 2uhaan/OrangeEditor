package com.ruhaan.orangeeditor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruhaan.orangeeditor.data.entity.ImageLayerEntity

@Dao
// table name image_layers
interface ImageLayerDao {

  @Query("SELECT * FROM image_layers WHERE editorId = :editorId")
  suspend fun getImagerLayerByEditorStateId(editorId: String): List<ImageLayerEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveImageLayerEntity(imageLayerEntity: ImageLayerEntity)
}
