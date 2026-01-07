package com.ruhaan.orangeeditor.domain.model

import com.ruhaan.orangeeditor.domain.model.format.CanvasFormat
import org.junit.Assert.*
import org.junit.Test

class CanvasFormatTest {

  @Test
  fun `canvasFormats list contains exactly 4 formats`() {
    // Given
    val formats = CanvasFormat.entries

    // Then
    assertEquals(4, formats.size)
  }

  @Test
  fun `canvasFormats contains Story format with correct properties`() {
    // Given
    val storyFormat = CanvasFormat.entries.find { it.title == "Story" }

    // Then
    assertNotNull(storyFormat)
    assertEquals("Story", storyFormat?.title)
    assertEquals("9:16", storyFormat?.aspectRatio)
  }

  @Test
  fun `canvasFormats contains Post format with correct properties`() {
    // Given
    val postFormat = CanvasFormat.entries.find { it.title == "Post" }

    // Then
    assertNotNull(postFormat)
    assertEquals("Post", postFormat?.title)
    assertEquals("1:1", postFormat?.aspectRatio)
  }

  @Test
  fun `canvasFormats contains Portrait format with correct properties`() {
    // Given
    val portraitFormat = CanvasFormat.entries.find { it.title == "Portrait" }

    // Then
    assertNotNull(portraitFormat)
    assertEquals("Portrait", portraitFormat?.title)
    assertEquals("4:5", portraitFormat?.aspectRatio)
  }

  @Test
  fun `canvasFormats contains Thumbnail format with correct properties`() {
    // Given
    val thumbnailFormat = CanvasFormat.entries.find { it.title == "Thumbnail" }

    // Then
    assertNotNull(thumbnailFormat)
    assertEquals("Thumbnail", thumbnailFormat?.title)
    assertEquals("16:9", thumbnailFormat?.aspectRatio)
  }

  @Test
  fun `all formats have unique titles`() {
    // Given
    val formats = CanvasFormat.entries
    val titles = formats.map { it.title }

    // Then
    assertEquals(titles.size, titles.toSet().size)
  }
}
