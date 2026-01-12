package com.ruhaan.orangeeditor.data.repository

import com.ruhaan.orangeeditor.data.dao.EditorStateDao
import com.ruhaan.orangeeditor.data.dao.ImageLayerDao
import com.ruhaan.orangeeditor.data.dao.TextLayerDao
import com.ruhaan.orangeeditor.data.entity.EditorStateEntity
import com.ruhaan.orangeeditor.data.mapper.toDomain
import com.ruhaan.orangeeditor.data.mapper.toEntity
import com.ruhaan.orangeeditor.data.storage.StorageType
import com.ruhaan.orangeeditor.domain.model.layer.EditorState
import com.ruhaan.orangeeditor.domain.model.layer.ImageLayer
import com.ruhaan.orangeeditor.domain.model.layer.TextLayer
import com.ruhaan.orangeeditor.domain.repository.OrangeRepository
import com.ruhaan.orangeeditor.util.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OrangeRepositoryImpl(
    private val editorStateDao: EditorStateDao,
    private val imageLayerDao: ImageLayerDao,
    private val textLayerDao: TextLayerDao,
    private val storage: Storage,
) : OrangeRepository {

  override fun getAllEditorState(): Flow<List<EditorStateEntity>> =
      editorStateDao.getAllEditorStateEntity()

  override suspend fun getEditorStatById(editorId: String): EditorState =
      withContext(Dispatchers.IO) {
        val editorStateEntity = editorStateDao.getEditorStateEntityById(editorId = editorId)
        val allImageLayerEntity = imageLayerDao.getImagerLayerByEditorStateId(editorId = editorId)
        val allTextLayerEntity = textLayerDao.geTextLayerByEditorStateId(editorId = editorId)

        val allImageLayer = coroutineScope {
          allImageLayerEntity
              .map { entity ->
                async { entity.toDomain(storage.loadBitmapFromPath(entity.bitmapStoredPath)) }
              }
              .awaitAll()
        }

        val allTextLayer = allTextLayerEntity.map { textLayerEntity -> textLayerEntity.toDomain() }
        val combineList = (allImageLayer + allTextLayer).sortedBy { it.zIndex }
        val editorState = editorStateEntity.toDomain(layers = combineList)
        return@withContext editorState
      }

  override suspend fun saveEditorState(editorState: EditorState) =
      withContext(Dispatchers.IO) {
        // Delete old entry
        val oldEditorState =  editorStateDao.getEditorStateEntityByIdCanNull(editorState.editorId)

        oldEditorState?.let { deleteEditorStateById(oldEditorState.editorId) }

        // Create new entry
        val previewBitmap =
            storage.getBitmapFromLayer(
                layers = editorState.layers,
                canvasFormat = editorState.canvasFormat,
                canvasScreenSize = editorState.canvasSize,
            )
        val previewUrl =
            storage.saveBitmapToAppStorage(
                bitmap = previewBitmap,
                storageType = StorageType.PREVIEW_DIR,
                quality = 10,
            )

        val editorStateEntity = editorState.toEntity(previewUrl = previewUrl)
        editorStateDao.saveEditorStateEntity(editorStateEntity = editorStateEntity)
        editorState.layers.forEach { layer ->
          when (layer) {
            is ImageLayer -> {
              layer.bitmap?.let { nonNumBitmap ->
                val bitmapPath =
                    storage.saveBitmapToAppStorage(
                        bitmap = nonNumBitmap,
                        storageType = StorageType.IMAGES_DIR,
                    )
                bitmapPath?.let {
                  val imageLayerEntity =
                      layer.toEntity(editorState.editorId, bitmapPath = bitmapPath)
                  imageLayerDao.saveImageLayerEntity(imageLayerEntity = imageLayerEntity)
                }
              }
            }
            is TextLayer -> {
              val textLayerEntity = layer.toEntity(editorState.editorId)
              textLayerDao.saveTextLayerEntity(textLayerEntity = textLayerEntity)
            }
          }
        }
      }

  override suspend fun deleteEditorStateById(editorId: String) =
      withContext(Dispatchers.IO) {
        val allImageLayerEntity = imageLayerDao.getImagerLayerByEditorStateId(editorId)
        val editorState = editorStateDao.getEditorStateEntityById(editorId)
        editorState.previewUrl?.let { path -> storage.deleteBitmapFromPath(path) }
        allImageLayerEntity.forEach { storage.deleteBitmapFromPath(it.bitmapStoredPath) }
        editorStateDao.deleteEditorStateEntity(editorId)
      }
}
