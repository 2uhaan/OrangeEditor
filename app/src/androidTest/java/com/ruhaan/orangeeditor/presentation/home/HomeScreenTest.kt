package com.ruhaan.orangeeditor.presentation.home


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysHeaderAndGrid() {
        // When
        composeTestRule.setContent {
            HomeScreen(onFormatSelected = {})
        }

        // Then - verify header is present
        composeTestRule.onNodeWithText("Orange Editor").assertIsDisplayed()

        // Then - verify all canvas formats are present
        composeTestRule.onNodeWithText("Story").assertIsDisplayed()
        composeTestRule.onNodeWithText("Post").assertIsDisplayed()
        composeTestRule.onNodeWithText("Portrait").assertIsDisplayed()
        composeTestRule.onNodeWithText("Thumbnail").assertIsDisplayed()
    }
}
