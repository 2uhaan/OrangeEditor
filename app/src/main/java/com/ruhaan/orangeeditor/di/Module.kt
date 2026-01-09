package com.ruhaan.orangeeditor.di

import android.content.Context
import com.ruhaan.orangeeditor.data.dao.EditorStateDao
import com.ruhaan.orangeeditor.data.dao.ImageLayerDao
import com.ruhaan.orangeeditor.data.dao.TextLayerDao
import com.ruhaan.orangeeditor.data.database.OrangeDatabase
import com.ruhaan.orangeeditor.data.repository.OrangeRepositoryImpl
import com.ruhaan.orangeeditor.domain.repository.OrangeRepository
import com.ruhaan.orangeeditor.util.AppStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

  @Provides
  @Singleton
  fun provideDb(@ApplicationContext context: Context) =
      OrangeDatabase.getInstance(context = context)

  @Provides
  @Singleton
  fun provideEditorStateDao(db: OrangeDatabase): EditorStateDao = db.editorStateDao()

  @Provides
  @Singleton
  fun provideImageLayerDao(db: OrangeDatabase): ImageLayerDao = db.imageLayerDao()

  @Provides @Singleton fun provideTextLayerDao(db: OrangeDatabase): TextLayerDao = db.textLayerDao()

  @Provides
  @Singleton
  fun provideAppStorage(@ApplicationContext context: Context): AppStorage =
      AppStorage(context = context)

  @Provides
  @Singleton
  fun provideOrangeRepository(
      editorStateDao: EditorStateDao,
      imageLayerDao: ImageLayerDao,
      textLayerDao: TextLayerDao,
      appStorage: AppStorage,
  ): OrangeRepository =
      OrangeRepositoryImpl(
          editorStateDao = editorStateDao,
          imageLayerDao = imageLayerDao,
          textLayerDao = textLayerDao,
          appStorage = appStorage,
      )
}
