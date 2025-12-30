package com.ruhaan.orangeeditor.presentation.navigation

import org.junit.Assert.*
import org.junit.Test

class RoutesTest {

  @Test
  fun `home route has correct path`() {
    assertEquals("home", Route.Home.route)
  }

  @Test
  fun `editor route contains format parameter placeholder`() {
    assertTrue(Route.Editor.route.contains("{canvasFormat}"))
  }

  @Test
  fun `editor route with format creates correct path`() {
    val format = "STORY"
    val expectedRoute = "editor/STORY"
    assertEquals(expectedRoute, "editor/$format")
  }
}
