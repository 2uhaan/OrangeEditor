package com.ruhaan.orangeeditor.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "text_layers",
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
data class TextLayerEntity(
    @PrimaryKey val id: String,
    @Embedded val base: LayerBase,
    val editorId: String,
    val text: String,
    val colorArgb: Int,
    val fontSizeInPx: Int,
    /** This only support only Normal(0) and Bold(1). */
    val fontWeightValue: Int,
    /** This only support only Normal(0) and Italic(1). */
    val fontStyleValue: Int,
    val displayName: String,
)
