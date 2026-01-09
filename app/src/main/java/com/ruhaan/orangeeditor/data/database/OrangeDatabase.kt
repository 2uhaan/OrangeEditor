package com.ruhaan.orangeeditor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ruhaan.orangeeditor.data.dao.EditorStateDao
import com.ruhaan.orangeeditor.data.dao.ImageLayerDao
import com.ruhaan.orangeeditor.data.dao.TextLayerDao
import com.ruhaan.orangeeditor.data.entity.EditorStateEntity
import com.ruhaan.orangeeditor.data.entity.ImageLayerEntity
import com.ruhaan.orangeeditor.data.entity.TextLayerEntity

@Database(
    entities = [EditorStateEntity::class, ImageLayerEntity::class, TextLayerEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class OrangeDatabase : RoomDatabase() {

  abstract fun editorStateDao(): EditorStateDao

  abstract fun imageLayerDao(): ImageLayerDao

  abstract fun textLayerDao(): TextLayerDao

  companion object {
    @Volatile private var INSTANCE: OrangeDatabase? = null

    fun getInstance(context: Context): OrangeDatabase {
      return INSTANCE
          ?: synchronized(this) {
            INSTANCE
                ?: Room.databaseBuilder(
                        context.applicationContext,
                        OrangeDatabase::class.java,
                        "orange.db",
                    )
                    .addCallback(
                        object : Callback() {
                          override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            db.execSQL("PRAGMA foreign_keys=ON")
                          }
                        }
                    )
                    .fallbackToDestructiveMigration(true) // drop all table if schema not match
                    .build()
                    .also { INSTANCE = it }
          }
    }
  }
}
