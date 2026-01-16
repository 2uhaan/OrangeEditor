package com.ruhaan.orangeeditor.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ruhaan.orangeeditor.domain.model.layer.Adjustment
import com.ruhaan.orangeeditor.domain.model.layer.ImageFilter

@Entity(
    tableName = "image_layers",
    foreignKeys =
        [
            ForeignKey(
                entity = EditorStateEntity::class,
                parentColumns = ["editorId"],
                childColumns = ["editorId"],
                onDelete = ForeignKey.CASCADE,
            )
        ],
    indices = [Index("editorId")],
)
data class ImageLayerEntity(
    @PrimaryKey val id: String,
    @Embedded val base: LayerBase,
    @Embedded val adjustment: Adjustment,
    val editorId: String,
    val imageFilter: ImageFilter,
    val originalWidth: Int,
    val originalHeight: Int,
    val displayName: String,
)
